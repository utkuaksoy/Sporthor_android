//
//  CreatePostContracts.swift
//  Sporthor
//
//  Created by derTurke on 21.04.2025.
//
//

import UIKit
import Photos

protocol CreatePostPresenterProtocol: BasePresenterProtocol {
    var view: CreatePostPresenterDelegate? { get set }
    var interactor: CreatePostInteractorProtocol { get set }
    var router: CreatePostRouterProtocol { get set }
    var assetArray: [AssetModel] { get set }
    var multipleSelected: Bool { get set }
    
    func viewDidLoad()
    func viewWillAppear()
    func didTappedCKButton(_ tag: Int)
    func didSelectItem(at indexPath: IndexPath)
    func didTappedCloseButton()
    func didTappedNextButton()
}

protocol CreatePostPresenterDelegate: BasePresenterDelegate {
    func prepareNavigationBar()
    func prepareNavigationDelegate()
    func prepareUI()
    func reloadData()
    func changeMulitpleSelectedButtonImage(_ image: UIImage)
    func previewImage(_ image: UIImage)
    func previewVideo(with avAsset: AVAsset)
    func pauseVideo()
}

protocol CreatePostInteractorProtocol: BaseInteractorProtocol {
    var delegate: CreatePostInteractorDelegate? { get set }
}

protocol CreatePostInteractorDelegate: BaseInteractorDelegate {
}

protocol CreatePostRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: CreatePostRoutes)
}

enum CreatePostRoutes {
    case showAlertConroller(_ alertController: UIAlertController)
    case close
    case openCreatePostDetail(_ model: [AssetModel])
    case camera
}
