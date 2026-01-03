//
//  CreatePostPresenter.swift
//  Sporthor
//
//  Created by derTurke on 21.04.2025.
//

import Foundation
import CommonKit

final class CreatePostPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: CreatePostPresenterDelegate? {
        get { return self.baseView as? CreatePostPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: CreatePostInteractorProtocol {
        get { return self.baseInteractor as! CreatePostInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: CreatePostRouterProtocol {
        get { return self.baseRouter as! CreatePostRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: CreatePostPresenterDelegate,
         interactor: CreatePostInteractorProtocol,
         router: CreatePostRouterProtocol) {
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
    }

    var assetArray: [AssetModel] = []
    var multipleSelected: Bool = false
    private var selectedAssetIdentifiers: [String] = []
}

// MARK: - CreatePostPresenterProtocol
extension CreatePostPresenter: CreatePostPresenterProtocol {
    func viewDidLoad() {
        view?.didSetBackgroundColor(.black)
        view?.didSetTitle("Yeni Gönderi")
        view?.prepareNavigationBar()
        view?.prepareUI()
    }
    
    func viewWillAppear() {
        view?.prepareNavigationDelegate()
        checkPhotoLibraryPermission()
    }

    func didTappedCKButton(_ tag: Int) {
        switch tag {
        case 1:
            toggleMultipleSelection()
        case 2:
            view?.pauseVideo()
            navigate(.camera)
        default: break
        }
    }

    func didSelectItem(at indexPath: IndexPath) {
        let assetId = assetArray[indexPath.item].asset?.localIdentifier ?? ""
        
        if multipleSelected {
            handleMultipleSelection(at: indexPath, assetId: assetId)
        } else {
            selectSingleAsset(at: indexPath, assetId: assetId)
        }

        view?.reloadData()
    }

    func didTappedCloseButton() {
        navigate(.close)
    }

    func didTappedNextButton() {
        let selectedAssets = selectedAssetIdentifiers.compactMap { id in
            assetArray.first { $0.asset?.localIdentifier == id }
        }

        guard !selectedAssets.isEmpty else {
            showAlert(type: .warning,
                      title: "Uyarı",
                      message: "İlerleyebilmek için en az 1 adet fotoğraf veya videoyu seçmelisiniz.")
            return
        }

        navigate(.openCreatePostDetail(selectedAssets))
    }
}

// MARK: - Private Methods
private extension CreatePostPresenter {
    func navigate(_ route: CreatePostRoutes) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.router.handleRouter(route)
        }
    }

    func checkPhotoLibraryPermission() {
        BaseHelper.shared.checkPhotoLibraryPermission { [weak self] authorized in
            guard let self else { return }
            authorized ? fetchAssets() : showPermissionAlert()
        }
    }

    func fetchAssets() {
        DispatchQueue.global(qos: .userInitiated).async { [weak self] in
            guard let self else { return }
            let assets = BaseHelper.shared.fetchPhotoLibraryAssets().reversed()

            DispatchQueue.main.async { [weak self] in
                guard let self else { return }
                self.assetArray = assets.map { item in
                    let id = item.localIdentifier
                    return AssetModel(
                        asset: item,
                        isSelected: self.selectedAssetIdentifiers.contains(id),
                        index: self.selectedAssetIdentifiers.firstIndex(of: id).map { $0 + 1 }
                    )
                }

                self.setInitialPreview()
                self.view?.reloadData()
            }
        }
    }

    func setInitialPreview() {
        if let lastId = selectedAssetIdentifiers.last,
           let asset = assetArray.first(where: { $0.asset?.localIdentifier == lastId }) {
            preparePreview(asset)
        } else if let first = assetArray.first {
            selectAsset(first)
        }
    }

    func selectAsset(_ asset: AssetModel) {
        guard let id = asset.asset?.localIdentifier,
              let index = assetArray.firstIndex(where: { $0.asset?.localIdentifier == id }) else { return }

        selectedAssetIdentifiers = [id]
        assetArray.indices.forEach {
            assetArray[$0].isSelected = ($0 == index)
            assetArray[$0].index = ($0 == index) ? 1 : nil
        }

        preparePreview(assetArray[index])
    }

    func toggleMultipleSelection() {
        multipleSelected.toggle()
        let image = multipleSelected ? Asset.multipleSelectedWhiteFill.image : Asset.multipleSelectedWhite.image
        view?.changeMulitpleSelectedButtonImage(image)

        if !multipleSelected {
            selectedAssetIdentifiers = selectedAssetIdentifiers.last.map { [$0] } ?? []
            updateSelectedIndexes()

            if let lastId = selectedAssetIdentifiers.last,
               let asset = assetArray.first(where: { $0.asset?.localIdentifier == lastId }) {
                preparePreview(asset)
            }
        }

        view?.reloadData()
    }

    func handleMultipleSelection(at indexPath: IndexPath, assetId: String) {
        var item = assetArray[indexPath.item]
        if item.isSelected {
            item.isSelected = false
            assetArray[indexPath.item] = item

            selectedAssetIdentifiers.removeAll { $0 == assetId }
            updateSelectedIndexes()

            if let lastId = selectedAssetIdentifiers.last,
               let asset = assetArray.first(where: { $0.asset?.localIdentifier == lastId }) {
                preparePreview(asset)
            }

        } else {
            guard selectedAssetIdentifiers.count < 10 else {
                showAlert(type: .warning, message: "En fazla 10 adet seçim yapabilirsiniz.")
                return
            }

            item.isSelected = true
            selectedAssetIdentifiers.append(assetId)
            assetArray[indexPath.item] = item

            updateSelectedIndexes()
            preparePreview(item)
        }
    }


    func selectSingleAsset(at indexPath: IndexPath, assetId: String) {
        selectedAssetIdentifiers = [assetId]
        assetArray.indices.forEach {
            assetArray[$0].isSelected = ($0 == indexPath.item)
            assetArray[$0].index = ($0 == indexPath.item) ? 1 : nil
        }

        preparePreview(assetArray[indexPath.item])
    }

    func updateSelectedIndexes() {
        assetArray.indices.forEach {
            let id = assetArray[$0].asset?.localIdentifier ?? ""
            if let index = selectedAssetIdentifiers.firstIndex(of: id) {
                assetArray[$0].index = index + 1
                assetArray[$0].isSelected = true
            } else {
                assetArray[$0].index = nil
                assetArray[$0].isSelected = false
            }
        }
    }

    func preparePreview(_ assetModel: AssetModel?) {
        guard let asset = assetModel?.asset else { return }
        Task { @MainActor in
            if asset.mediaType == .image {
                if let image = await BaseHelper.shared.requestImage(for: asset) {
                    view?.previewImage(image)
                }
            } else {
                if let video = await BaseHelper.shared.requestVideoAsset(for: asset) {
                    view?.previewVideo(with: video)
                }
            }
        }
    }

    func showPermissionAlert() {
        let alert = BaseHelper.shared.showPermissionAlert(for: .gallery)
        navigate(.showAlertConroller(alert))
    }
}

// MARK: - CreatePostInteractorDelegate
extension CreatePostPresenter: CreatePostInteractorDelegate {}
