//
//  CameraContracts.swift
//  Sporthor
//
//  Created by derTurke on 24.04.2025.
//

import UIKit
import AVFoundation

protocol CameraPresenterProtocol: BasePresenterProtocol {
    var view: CameraPresenterDelegate? { get set }
    var interactor: CameraInteractorProtocol { get set }
    var router: CameraRouterProtocol { get set }
    var feedType: FeedType { get set }
    var cropRect: CGRect { get set }
    var convertedCropRect: CGRect { get set }
    
    func viewDidLoad()
    func viewWillAppear()
    func didTappedBackButton()
    func didFinishPhotoCapture(image: UIImage?)
    func didFinishVideoCapture(video: URL?)

    func handleButtonTouchDown()
    func handleButtonTouchUpInside()
    func handleButtonTouchUpOutside()
    func photoCaptured(data: Data?)
    func videoRecorded(url: URL, viewSize: CGSize)
    func didStartRecord()
}

protocol CameraPresenterDelegate: BasePresenterDelegate {
    func prepareUI()
    func prepareNavigationDelegate()
    func prepareNavigationBar()
    func didAddViewDoubleTapGestureRecognizer(_ tapGesture: UITapGestureRecognizer)
    func animateButtonState(isRecording: Bool, isTapped: Bool)
    func setupCameraPreview(previewLayer: AVCaptureVideoPreviewLayer)
}

protocol CameraInteractorProtocol: BaseInteractorProtocol {
    var delegate: CameraInteractorDelegate? { get set }
}

protocol CameraInteractorDelegate: BaseInteractorDelegate {
}

protocol CameraRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: CameraRoutes)
}

enum CameraRoutes {
    case showAlertController(_ alertController: UIAlertController)
    case back
    case preview(image: UIImage?,
                 video: URL?,
                 feedType: FeedType,
                 previewDelegate: PreviewDelegate?)
}

enum FeedType {
    case post
    case story
}
