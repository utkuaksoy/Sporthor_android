//
//  CreatePostDetailPresenter.swift
//  Sporthor
//
//  Created by derTurke on 23.04.2025.
//
//

import UIKit
import AVFoundation
import CommonKit
import ComponentKit
import Combine

final class CreatePostDetailPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: CreatePostDetailPresenterDelegate? {
        get { return self.baseView as? CreatePostDetailPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: CreatePostDetailInteractorProtocol {
        get { return self.baseInteractor as! CreatePostDetailInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: CreatePostDetailRouterProtocol {
        get { return self.baseRouter as! CreatePostDetailRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: CreatePostDetailPresenterDelegate,
         interactor: CreatePostDetailInteractorProtocol,
         router: CreatePostDetailRouterProtocol,
         model: [AssetModel] = []) {
        self.model = model
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
    }
    
    var model: [AssetModel]
    private var postDescription: String = ""
    private var images: [UIImage] = []
    private var videos: [VideoModel] = []
    private var request: CreatePostRequest = CreatePostRequest()
    
    // MARK: - Combine
    static let postCreated = PassthroughSubject<Void, Never>()
}

// MARK: - CreatePostDetailPresenterProtocol
extension CreatePostDetailPresenter: CreatePostDetailPresenterProtocol {
    func viewDidLoad() {
        view?.didSetBackgroundColor(.black)
        view?.didSetTitle("Yeni Gönderi")
        view?.prepareNavigationBar()
        view?.prepareUI()
    }
    
    func viewWillAppear() {
        view?.prepareNavigationDelegate()
    }
    
    private func navigate(_ routes: CreatePostDetailRoutes) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.router.handleRouter(routes)
        }
    }
    
    func textViewDidEndEditing(_ text: String, tag: Int) {
        postDescription = text
    }
    
    func textViewDidChange(_ text: String, tag: Int) {
        view?.beginUpdates()
    }
    
    func didTappedBackButton() {
        navigate(.back)
    }
    
    func didTappedShareButton() {
        guard !model.isEmpty else { return }

        Task {
            await processAssets()
        }
    }

    private func processAssets() async {
        images = []
        videos = []

        for (index, item) in model.enumerated() {
            guard let asset = item.asset else { continue }

            if asset.mediaType == .image {
                if let image = await BaseHelper.shared.requestImage(for: asset) {
                    let targetSize: CGSize

                    if image.isPortrait {
                        targetSize = CGSize(width: 768, height: 1024)
                    } else if image.isLandscape {
                        targetSize = CGSize(width: 1024, height: 768)
                    } else {
                        targetSize = CGSize(width: 1024, height: 1024)
                    }

                    let resizedImage = image.resize(to: targetSize)
                    images.append(resizedImage)
                }
            } else if asset.mediaType == .video {
                if let avAsset = await BaseHelper.shared.requestVideoAsset(for: asset) {
                    let targetSize: CGSize

                    if avAsset.isPortrait {
                        targetSize = CGSize(width: 768, height: 1024)
                    } else if avAsset.isLandscape {
                        targetSize = CGSize(width: 1024, height: 768)
                    } else {
                        targetSize = CGSize(width: 1024, height: 1024)
                    }

                    if let url = await BaseHelper.shared.convertAVAssetToURL(avAsset, size: targetSize),
                       !videos.contains(where: { $0.index == index }) {
                        videos.append(VideoModel(video: url, index: index))
                    }
                }
            }
        }
        uploadMultipleImage()
    }

    
    private func uploadMultipleImage() {
        if !images.isEmpty {
            Task {
                @MainActor in
                await interactor.uploadMultipleImages(images)
            }
        } else {
            uploadVideo()
        }
    }
    
    private func uploadVideo() {
        if !videos.isEmpty {
            for item in videos {
                Task {
                    @MainActor in
                    await interactor.uploadVideo(item.video, index: item.index)
                }
            }
        } else {
            createPost()
        }
    }
    
    private func createPost() {
        guard !request.media.isEmpty else { return }
        do {
            request.description = postDescription
            let request = try request.asDictionary()
            Task {
                @MainActor in
                await interactor.createPost(request)
            }
        } catch {
            showAlert(type: .error, title: "Hata", message: "Post yüklenirken hata oluştu. Lütfen daha sonra tekrar deneyiniz.")
        }
        
    }
}

// MARK: - CreatePostDetailInteractorDelegate
extension CreatePostDetailPresenter: CreatePostDetailInteractorDelegate {
    func didUploadMultipleImages(filePaths: [ImageUploadResponseModel]) {
        for item in filePaths {
            request.media.append(MediaItem(url: item.filePath, type: 0))
        }
        uploadVideo()
    }
    
    func didUploadVideo(filePath: String, index: Int) {
        request.media.insert(MediaItem(url: filePath, type: 1), at: index)
        createPost()
    }
    
    func didCreatePost() {
        Self.postCreated.send()
        showAlert(delegate: self,
                  type: .success,
                  title: "Başarılı",
                  message: "Postunuz başarıyla oluşturulmuştur.")
    }
}

extension CreatePostDetailPresenter: AlertViewDelegate {
    func didTappedAlertButton(_ tag: Int) {
        navigate(.dismiss)
    }
}
