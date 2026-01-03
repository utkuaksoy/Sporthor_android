//
//  PreviewPresenter.swift
//  Sporthor
//
//  Created by derTurke on 24.04.2025.
//
//

import UIKit
import Photos
import CommonKit

final class PreviewPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: PreviewPresenterDelegate? {
        get { return self.baseView as? PreviewPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: PreviewInteractorProtocol {
        get { return self.baseInteractor as! PreviewInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: PreviewRouterProtocol {
        get { return self.baseRouter as! PreviewRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: PreviewPresenterDelegate,
         interactor: PreviewInteractorProtocol,
         router: PreviewRouterProtocol,
         image: UIImage?,
         video: URL?,
         feedType: FeedType,
         previewDelegate: PreviewDelegate?) {
        self.image = image
        self.video = video
        self.feedType = feedType
        self.previewDelegate = previewDelegate
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
    }
    
    var image: UIImage?
    var video: URL?
    var feedType: FeedType
    private weak var previewDelegate: PreviewDelegate?
    private var isTappedWriteLabel: Bool = false
    private var text: String = ""
}

// MARK: - PreviewPresenterProtocol
extension PreviewPresenter: PreviewPresenterProtocol {
    func viewDidLoad() {
        view?.didSetBackgroundColor(.black)
        view?.prepareNavigationBar()
        prepareUI()
        fetchGalleryLastItem()
    }
    
    func viewWillAppear() {
        view?.prepareNavigationBarDelegate()
    }
    
    private func navigate(_ routes: PreviewRoutes) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.router.handleRouter(routes)
        }
    }
    
    private func prepareUI() {
        if let _ = image {
            view?.prepareImageUI()
        } else if let _ = video {
            view?.prepareVideoUI()
        } else {
            navigate(.back)
        }
    }
    
    private func fetchGalleryLastItem() {
        guard feedType == .post else { return }
        guard let asset = BaseHelper.shared.fetchPhotoLibraryAssets().reversed().first else { return }
        Task {
            @MainActor in
            guard let image = await BaseHelper.shared.requestImage(for: asset) else { return }
            view?.prepareLibraryButtonImage(image)
        }
    }
    
    func didTappedBackButton() {
        navigate(.back)
    }
    
    func didTappedCKButton(_ tag: Int) {
        switch tag {
        case 0: // Library
            navigate(.backLibrary)
        case 1: // Continue
            feedType == .post ? convertPHAsset() : shareStory()
        default:
            break
        }
    }
    
    private func convertPHAsset() {
        Task { @MainActor in
            var assetModel: [AssetModel] = []
            
            if image != nil {
                if let asset = await BaseHelper.shared.fetchLatestLastAsset(type: .image) {
                    assetModel = [AssetModel(asset: asset, isSelected: true, index: 1)]
                }
            } else if video != nil {
                if let asset = await BaseHelper.shared.fetchLatestLastAsset(type: .video) {
                    assetModel = [AssetModel(asset: asset, isSelected: true, index: 1)]
                }
            }
            
            if !assetModel.isEmpty {
                navigate(.createPostDetail(assetModel))
            }
        }
    }
    
    private func shareStory() {
        if let image = image {
            if feedType == .story {
                view?.controlImageText()
            } else {
                uploadImage(image: image)
            }
        } else if let video = video {
            uploadVideo(video: video)
        } else {
            return
        }
    }
    
    func uploadImage(image: UIImage) {
        Task { @MainActor in
            await interactor.uploadImage(image)
        }
    }
    
    func uploadVideo(video: URL) {
        Task { @MainActor in
            await interactor.uploadVideo(video)
        }
    }
    
    func didTappedWriteLabel() {
        isTappedWriteLabel = true
        view?.changeNavigatonBarItems(true)
        view?.changeHiddenStoryContinueButton(true)
        view?.changeTextViewHidden(false)
        view?.changeTextLabelHidden(true)
        view?.focusedTextView()
    }
    
    func didTappedFinish() {
        isTappedWriteLabel = false
        view?.unfocusedTextView()
        view?.changeNavigatonBarItems(false)
        view?.changeHiddenStoryContinueButton(false)
        view?.changeTextViewHidden(true)
        view?.changeTextLabelHidden(false)
    }
    
    func textViewDidEndEditing(_ text: String) {
        self.text = text
        guard !text.isEmpty else {
            view?.changeTextViewHidden(true)
            view?.changeTextLabelHidden(true)
            return
        }
        
        view?.changeTextLabelText(text)
        didTappedFinish()
    }
    
    func didTappedTextLabel(_ text: String) {
        guard isTappedWriteLabel else { return }
        self.text = text
        guard !text.isEmpty else {
            view?.changeTextViewHidden(true)
            view?.changeTextLabelHidden(true)
            return
        }
        
        view?.changeTextViewText(text)
        view?.changeTextViewHidden(false)
        view?.changeTextLabelHidden(true)
        view?.focusedTextView()
    }
    
    func openLocations() {
        navigate(.locations(delegate: self))
    }
}

// MARK: - PreviewInteractorDelegate
extension PreviewPresenter: PreviewInteractorDelegate {
    func didUpload(filePath: String) {
        var mediaType: Int = 0
        if let _ = image {
            mediaType = 0
        } else if let _ = video {
            mediaType = 1
        } else {
            return
        }
        
        let request: [String: Any] = ["mediaUrl": filePath,
                                      "mediaType": mediaType]
        Task {
            @MainActor in
            await interactor.createStory(request)
        }
    }
    
    func didCreateStory() {
        navigate(.dismiss(previewDelegate: previewDelegate))
    }
}

extension PreviewPresenter: LocationDelegate {
    func didSelectLocation(name: String) {
        view?.didSelectLocation(name: name)
    }
}
