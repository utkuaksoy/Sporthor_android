import UIKit
import ComponentKit
import AVFoundation
import Photos

final class CameraViewController: BaseViewController {
    // MARK: - VIPER
    var presenter: CameraPresenterProtocol {
        get { return basePresenter as! CameraPresenterProtocol }
        set { basePresenter = newValue }
    }

    // MARK: - Camera Elements
    private var previewLayer: AVCaptureVideoPreviewLayer!

    // MARK: - UI
    private lazy var captureButton: CKButton = {
        let button = CKButton(buttonBackgroundColor: .white, cornerRadius: 35)
        button.translatesAutoresizingMaskIntoConstraints = false
        button.addTarget(self, action: #selector(buttonTouchDown), for: .touchDown)
        button.addTarget(self, action: #selector(buttonTouchUpInside), for: .touchUpInside)
        button.addTarget(self, action: #selector(buttonTouchUpOutside), for: .touchUpOutside)
        button.layer.zPosition = 10000
        return button
    }()

    // MARK: - Lifecycle
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.viewDidLoad()
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        presenter.viewWillAppear()
    }
    
    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        prepareCameraOverlay()
    }

    // MARK: - Touch Events
    @objc private func buttonTouchDown() {
        presenter.handleButtonTouchDown()
    }

    @objc private func buttonTouchUpInside() {
        presenter.handleButtonTouchUpInside()
    }

    @objc private func buttonTouchUpOutside() {
        presenter.handleButtonTouchUpOutside()
    }

    // MARK: - UI Update Methods
    private func animateButton(isRecording: Bool, isTapped: Bool) {
        let scale: CGFloat = isTapped ? 1.2 : 1.0
        let color: UIColor = isRecording ? DesignKitColorName.red600.color : .white
        let duration: TimeInterval = isRecording ? 0.2 : 0.1

        UIView.animate(withDuration: duration, animations: {
            self.captureButton.backgroundColor = color
            self.captureButton.transform = CGAffineTransform(scaleX: scale, y: scale)
        }) { _ in
            if !isTapped {
                UIView.animate(withDuration: 0.2) {
                    self.captureButton.transform = CGAffineTransform.identity
                }
            }
        }
    }
    
    private func prepareCameraOverlay() {
        guard presenter.feedType == .post else { return }
        guard let previewLayer = previewLayer else { return }
        
        let side = min(view.bounds.width, view.bounds.height)
        
        let layerCropRect = CGRect(x: 0,
                                   y: (view.bounds.height - side) / 2,
                                   width: side,
                                   height: side)
        presenter.cropRect = layerCropRect
        presenter.convertedCropRect = previewLayer.metadataOutputRectConverted(fromLayerRect: layerCropRect)
        view.subviews.first(where: { $0 is CKCameraOverlayView })?.removeFromSuperview()

        let overlay = CKCameraOverlayView(frame: view.bounds, transparentRect: layerCropRect)
        overlay.isUserInteractionEnabled = false
        view.addSubview(overlay)
    }
}

// MARK: - CameraPresenterDelegate
extension CameraViewController: CameraPresenterDelegate {
    func prepareUI() {
        view.addSubview(captureButton)
        NSLayoutConstraint.activate([
            captureButton.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            captureButton.bottomAnchor.constraint(equalTo: view.bottomAnchor, constant: -60),
            captureButton.widthAnchor.constraint(equalToConstant: 70),
            captureButton.heightAnchor.constraint(equalToConstant: 70)
        ])
    }

    func prepareNavigationDelegate() {
        if let nav = navigationController as? CustomNavigationController {
            nav.customDelegate = self
            nav.navigationItem.hidesBackButton = true
        }
    }

    func prepareNavigationBar() {
        if let nav = navigationController as? CustomNavigationController {
            nav.isCloseBackgroundBlackExist = true
            nav.navigationBar.titleTextAttributes = [
                .foregroundColor: UIColor.white,
                .font: UIFont.bold03Compact
            ]
        }
    }
    
    func didAddViewDoubleTapGestureRecognizer(_ tapGesture: UITapGestureRecognizer) {
        view.addGestureRecognizer(tapGesture)
    }

    func animateButtonState(isRecording: Bool, isTapped: Bool) {
        animateButton(isRecording: isRecording, isTapped: isTapped)
    }

    func setupCameraPreview(previewLayer: AVCaptureVideoPreviewLayer) {
        self.previewLayer = previewLayer
        previewLayer.frame = view.bounds
        previewLayer.videoGravity = .resizeAspectFill
        view.layer.insertSublayer(previewLayer, at: 0)
        prepareCameraOverlay()
    }
}

// MARK: - AVCapture Delegates
extension CameraViewController: AVCapturePhotoCaptureDelegate {
    func photoOutput(_ output: AVCapturePhotoOutput,
                     didFinishProcessingPhoto photo: AVCapturePhoto,
                     error: Error?) {
        presenter.photoCaptured(data: photo.fileDataRepresentation())
    }
}

extension CameraViewController: AVCaptureFileOutputRecordingDelegate {
    func fileOutput(_ output: AVCaptureFileOutput,
                    didStartRecordingTo fileURL: URL,
                    from connections: [AVCaptureConnection]) {
        presenter.didStartRecord()
    }

    func fileOutput(_ output: AVCaptureFileOutput,
                    didFinishRecordingTo outputFileURL: URL,
                    from connections: [AVCaptureConnection],
                    error: Error?) {
        presenter.videoRecorded(url: outputFileURL, viewSize: view.bounds.size)
    }
}

// MARK: - Navigation Delegate
extension CameraViewController: CustomNavigationControllerDelegate {
    func didTapButton(type: BarButtonItemType) {
        switch type {
        case .close:
            presenter.didTappedBackButton()
        default:
            break
        }
    }
}
