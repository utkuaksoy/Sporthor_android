//
//  CameraPresenter.swift
//  Sporthor
//
//  Created by derTurke on 24.04.2025.
//

import UIKit
import CommonKit
import AVFoundation
import Photos

final class CameraPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: CameraPresenterDelegate? {
        get { return self.baseView as? CameraPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: CameraInteractorProtocol {
        get { return self.baseInteractor as! CameraInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: CameraRouterProtocol {
        get { return self.baseRouter as! CameraRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Camera State
    private let captureSession = AVCaptureSession()
    private var photoOutput = AVCapturePhotoOutput()
    private var movieOutput = AVCaptureMovieFileOutput()
    private var currentCameraPosition: AVCaptureDevice.Position = .back
    private var isRecording = false
    private var didStartRecording = false
    private var recordTimer: Timer?
    
    // MARK: - Initialize
    init(view: CameraPresenterDelegate,
         interactor: CameraInteractorProtocol,
         router: CameraRouterProtocol,
         feedType: FeedType,
         previewDelegate: PreviewDelegate?) {
        self.feedType = feedType
        self.previewDelegate = previewDelegate
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
    }
    var feedType: FeedType
    private weak var previewDelegate: PreviewDelegate?
    var cropRect: CGRect = .zero
    var convertedCropRect: CGRect = .zero
}

// MARK: - CameraPresenterProtocol
extension CameraPresenter: CameraPresenterProtocol {
    private func navigate(_ routes: CameraRoutes) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.router.handleRouter(routes)
        }
    }
    
    func viewDidLoad() {
        view?.prepareNavigationBar()
        checkCameraPermission()
        addDoubleTapGestureRecognizer()
    }
    
    func viewWillAppear() {
        view?.prepareNavigationDelegate()
    }
    
    private func checkCameraPermission() {
        BaseHelper.shared.checkCameraPermission { [weak self] authorized in
            guard let self else { return }
            if authorized {
                DispatchQueue.main.async {
                    self.setupCamera()
                    self.view?.prepareUI()
                }
            } else {
                self.navigate(.showAlertController(BaseHelper.shared.showPermissionAlert(for: .camera)))
            }
        }
    }
    
    private func addDoubleTapGestureRecognizer() {
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(handleDoubleTap))
        tapGesture.numberOfTapsRequired = 2
        view?.didAddViewDoubleTapGestureRecognizer(tapGesture)
    }
    
    @objc private func handleDoubleTap() {
        switchCamera()
    }
    
    func handleButtonTouchDown() {
        didStartRecording = false
        recordTimer = Timer.scheduledTimer(withTimeInterval: 0.5, repeats: false) { [weak self] _ in
            self?.startRecording()
        }
        view?.animateButtonState(isRecording: false, isTapped: true)
    }
    
    func handleButtonTouchUpInside() {
        if didStartRecording {
            stopRecording()
        } else {
            recordTimer?.invalidate()
            capturePhoto()
        }
    }
    
    func handleButtonTouchUpOutside() {
        if didStartRecording {
            stopRecording()
        } else {
            recordTimer?.invalidate()
            view?.animateButtonState(isRecording: false, isTapped: false)
        }
    }
    
    func capturePhoto() {
        let settings = AVCapturePhotoSettings()
        
        settings.isHighResolutionPhotoEnabled = false
        
        photoOutput.capturePhoto(with: settings, delegate: view as! AVCapturePhotoCaptureDelegate)
    }
    
    func startRecording() {
        guard !movieOutput.isRecording else { return }
        
        isRecording = true
        didStartRecording = true
        view?.animateButtonState(isRecording: true, isTapped: true)
        
        let outputURL = FileManager.default.temporaryDirectory.appendingPathComponent("video_\(UUID().uuidString).mp4")
        movieOutput.startRecording(to: outputURL, recordingDelegate: view as! AVCaptureFileOutputRecordingDelegate)
    }
    
    func stopRecording() {
        guard movieOutput.isRecording else { return }
        
        movieOutput.stopRecording()
        isRecording = false
        didStartRecording = false
        view?.animateButtonState(isRecording: false, isTapped: false)
    }
    
    func didStartRecord() {
        didStartRecording = true
    }
    
    func photoCaptured(data: Data?) {
        guard let data = data, let image = UIImage(data: data) else {
            return
        }
        
        var fixedImage: UIImage
        fixedImage = fixImageOrientation(image)
        if feedType == .post {
            fixedImage = BaseHelper.shared.crop(image: fixedImage, cropRect: convertedCropRect)
        }
        
        UIImageWriteToSavedPhotosAlbum(fixedImage, nil, nil, nil)
        didFinishPhotoCapture(image: fixedImage)
    }
    
    private func fixImageOrientation(_ image: UIImage) -> UIImage {
        guard currentCameraPosition == .front else { return image }
        
        guard let cgImage = image.cgImage else { return image }
        
        return UIImage(cgImage: cgImage, scale: image.scale, orientation: .leftMirrored)
    }

    func videoRecorded(url: URL, viewSize: CGSize) {
        if feedType == .post {
            BaseHelper.shared.cropVideo(inputURL: url,
                                        cropRectInView: cropRect,
                                        viewSize: viewSize) { [weak self] croppedURL in
                guard let self = self else { return }
                if let croppedURL = croppedURL {
                    self.saveVideoToGallery(url: croppedURL)
                } else {
                    self.saveVideoToGallery(url: url)
                }
            }
        } else {
            saveVideoToGallery(url: url)
        }
    }
    
    func saveVideoToGallery(url: URL) {
        PHPhotoLibrary.shared().performChanges({
            PHAssetChangeRequest.creationRequestForAssetFromVideo(atFileURL: url)
        }) { [weak self] success, error in
            guard let self = self else { return }
            DispatchQueue.main.async {
                if success {
                    self.didFinishVideoCapture(video: url)
                }
            }
        }
    }

    
    func didTappedBackButton() {
        navigate(.back)
    }
    
    func didFinishPhotoCapture(image: UIImage?) {
        navigate(.preview(image: image,
                          video: nil,
                          feedType: feedType,
                          previewDelegate: previewDelegate))
    }
    
    func didFinishVideoCapture(video: URL?) {
        navigate(.preview(image: nil,
                          video: video,
                          feedType: feedType,
                          previewDelegate: previewDelegate))
    }
    
    private func setupCamera() {
        guard let device = AVCaptureDevice.default(for: .video),
              let input = try? AVCaptureDeviceInput(device: device) else { return }
        
        captureSession.beginConfiguration()
        
        captureSession.inputs.forEach { captureSession.removeInput($0) }
        
        if captureSession.canAddInput(input) {
            captureSession.addInput(input)
        }
        
        if let audioDevice = AVCaptureDevice.default(for: .audio),
           let audioInput = try? AVCaptureDeviceInput(device: audioDevice),
           captureSession.canAddInput(audioInput) {
            captureSession.addInput(audioInput)
        }
        
        if captureSession.canAddOutput(photoOutput) {
            captureSession.addOutput(photoOutput)
        }
        
        if captureSession.canAddOutput(movieOutput) {
            captureSession.addOutput(movieOutput)
        }
        
        captureSession.commitConfiguration()
        
        let previewLayer = AVCaptureVideoPreviewLayer(session: captureSession)
        previewLayer.videoGravity = .resizeAspectFill
        
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.view?.setupCameraPreview(previewLayer: previewLayer)
        }
        
        DispatchQueue.global(qos: .userInitiated).async { [weak self] in
            guard let self else { return }
            self.captureSession.startRunning()
        }
    }
    
    private func switchCamera() {
        captureSession.beginConfiguration()
        
        guard let currentInput = captureSession.inputs.first as? AVCaptureDeviceInput else {
            captureSession.commitConfiguration()
            return
        }
        
        captureSession.removeInput(currentInput)
        
        currentCameraPosition = currentCameraPosition == .back ? .front : .back
        
        guard let newDevice = AVCaptureDevice.default(.builtInWideAngleCamera, for: .video, position: currentCameraPosition),
              let newInput = try? AVCaptureDeviceInput(device: newDevice),
              captureSession.canAddInput(newInput) else {
            captureSession.commitConfiguration()
            return
        }
        
        captureSession.addInput(newInput)
        captureSession.commitConfiguration()
    }
}

// MARK: - CameraInteractorDelegate
extension CameraPresenter: CameraInteractorDelegate {
    
}
