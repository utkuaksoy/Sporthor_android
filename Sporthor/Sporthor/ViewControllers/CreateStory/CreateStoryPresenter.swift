//
//  CreateStoryPresenter.swift
//  Sporthor
//
//  Created by derTurke on 26.04.2025.
//
//

import Foundation
import CommonKit

final class CreateStoryPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: CreateStoryPresenterDelegate? {
        get { return self.baseView as? CreateStoryPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: CreateStoryInteractorProtocol {
        get { return self.baseInteractor as! CreateStoryInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: CreateStoryRouterProtocol {
        get { return self.baseRouter as! CreateStoryRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: CreateStoryPresenterDelegate,
         interactor: CreateStoryInteractorProtocol,
         router: CreateStoryRouterProtocol,
         previewDelegate: PreviewDelegate?) {
        self.previewDelegate = previewDelegate
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
    }
    
    var assetArray: [AssetModel] = []
    private weak var previewDelegate: PreviewDelegate?
}

// MARK: - CreateStoryPresenterProtocol
extension CreateStoryPresenter: CreateStoryPresenterProtocol {
    func viewDidLoad() {
        view?.prepareNavigationBar()
        view?.didSetTitle("Hikaye Ekle")
        view?.prepareUI()
    }
    
    func viewWillAppear() {
        view?.prepareNavigationDelegate()
        checkPhotoLibrary()
    }
    
    private func navigate(_ routes: CreateStoryRoutes) {
        router.handleRouter(routes)
    }
    
    private func checkPhotoLibrary() {
        BaseHelper.shared.checkPhotoLibraryPermission { [weak self] authorized in
            guard let self else { return }
            
            if authorized {
                DispatchQueue.global(qos: .userInitiated).async { [weak self] in
                    guard let self else { return }
                    let assets = BaseHelper.shared.fetchPhotoLibraryAssets().reversed()

                    DispatchQueue.main.async {
                        self.assetArray = assets.compactMap { item -> AssetModel in
                            AssetModel(asset: item)
                        }
                        self.view?.reloadData()
                    }
                }
            } else {
                let alertController = BaseHelper.shared.showPermissionAlert(for: .gallery)
                self.navigate(.showAlertConroller(alertController))
            }
        }
    }
    
    func didTappedClose() {
        navigate(.close)
    }
    
    func didTappedCameraView() {
        navigate(.camera(previewDelegate: previewDelegate))
    }
    
    func didSelectItem(at indexPath: IndexPath) {
        Task {
            @MainActor in
            guard let selectedAsset = assetArray[safe: indexPath.item],
                  let asset = selectedAsset.asset else { return }
            if asset.mediaType == .image {
                guard let image = await BaseHelper.shared.requestImage(for: asset) else { return }
                navigate(.preview(image: image,
                                  video: nil,
                                  feedType: .story,
                                  previewDelegate: previewDelegate))
            } else {
                guard let videoAsset = await BaseHelper.shared.requestVideoAsset(for: asset),
                      let video = await BaseHelper.shared.convertAVAssetToURL(videoAsset) else { return }
                navigate(.preview(image: nil,
                                  video: video,
                                  feedType: .story,
                                  previewDelegate: previewDelegate))
            }
        }
    }
}

// MARK: - CreateStoryInteractorDelegate
extension CreateStoryPresenter: CreateStoryInteractorDelegate {

}
