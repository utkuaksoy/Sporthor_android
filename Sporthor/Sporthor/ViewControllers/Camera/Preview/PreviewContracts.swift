//
//  PreviewContracts.swift
//  Sporthor
//
//  Created by derTurke on 24.04.2025.
//
//

import UIKit

protocol PreviewPresenterProtocol: BasePresenterProtocol {
    var view: PreviewPresenterDelegate? { get set }
    var interactor: PreviewInteractorProtocol { get set }
    var router: PreviewRouterProtocol { get set }
    var image: UIImage? { get set }
    var video: URL? { get set }
    var feedType: FeedType { get set }
    
    func viewDidLoad()
    func viewWillAppear()
    func didTappedBackButton()
    func didTappedCKButton(_ tag: Int)
    func didTappedWriteLabel()
    func didTappedFinish()
    func textViewDidEndEditing(_ text: String)
    func didTappedTextLabel(_ text: String)
    func uploadImage(image: UIImage)
    func uploadVideo(video: URL)
    func openLocations()
}

protocol PreviewPresenterDelegate: BasePresenterDelegate {
    func prepareNavigationBar()
    func prepareNavigationBarDelegate()
    func prepareImageUI()
    func prepareVideoUI()
    func prepareLibraryButtonImage(_ image: UIImage)
    func changeHiddenStoryContinueButton(_ isHidden: Bool)
    func changeNavigatonBarItems(_ isTappedWriteLabel: Bool)
    func focusedTextView()
    func changeTextViewHidden(_ isHidden: Bool)
    func changeTextLabelHidden(_ isHidden: Bool)
    func changeTextLabelText(_ text: String)
    func changeTextViewText(_ text: String)
    func unfocusedTextView()
    func controlImageText()
    func controlVideoText(_ video: URL)
    func didSelectLocation(name: String)
}

protocol PreviewInteractorProtocol: BaseInteractorProtocol {
    var delegate: PreviewInteractorDelegate? { get set }
    func uploadImage(_ image: UIImage) async
    func uploadVideo(_ url: URL) async
    func createStory(_ request: [String: Any]) async
}

protocol PreviewInteractorDelegate: BaseInteractorDelegate {
    func didUpload(filePath: String)
    func didCreateStory()
}

protocol PreviewRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: PreviewRoutes)
}

enum PreviewRoutes {
    case back
    case backLibrary
    case createPostDetail([AssetModel])
    case dismiss(previewDelegate: PreviewDelegate?)
    case locations(delegate: LocationDelegate?)
}

protocol PreviewDelegate: AnyObject {
    func didAddStory()
}
