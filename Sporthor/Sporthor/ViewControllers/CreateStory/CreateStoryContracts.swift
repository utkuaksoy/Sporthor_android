//
//  CreateStoryContracts.swift
//  Sporthor
//
//  Created by derTurke on 26.04.2025.
//
//

import UIKit

protocol CreateStoryPresenterProtocol: BasePresenterProtocol {
    var view: CreateStoryPresenterDelegate? { get set }
    var interactor: CreateStoryInteractorProtocol { get set }
    var router: CreateStoryRouterProtocol { get set }
    var assetArray: [AssetModel] { get set }
    
    func viewDidLoad()
    func viewWillAppear()
    func didTappedClose()
    func didTappedCameraView()
    func didSelectItem(at indexPath: IndexPath)
}

protocol CreateStoryPresenterDelegate: BasePresenterDelegate {
    func prepareUI()
    func prepareNavigationBar()
    func prepareNavigationDelegate()
    func reloadData()
}

protocol CreateStoryInteractorProtocol: BaseInteractorProtocol {
    var delegate: CreateStoryInteractorDelegate? { get set }
}

protocol CreateStoryInteractorDelegate: BaseInteractorDelegate {
}

protocol CreateStoryRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: CreateStoryRoutes)
}

enum CreateStoryRoutes {
    case showAlertConroller(_ alertController: UIAlertController)
    case close
    case camera(previewDelegate: PreviewDelegate?)
    case preview(image: UIImage?,
                 video: URL?,
                 feedType: FeedType,
                 previewDelegate: PreviewDelegate?)
}
