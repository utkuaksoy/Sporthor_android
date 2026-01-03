//
//  CreatePostDetailContracts.swift
//  Sporthor
//
//  Created by derTurke on 23.04.2025.
//
//

import UIKit

protocol CreatePostDetailPresenterProtocol: BasePresenterProtocol {
    var view: CreatePostDetailPresenterDelegate? { get set }
    var interactor: CreatePostDetailInteractorProtocol { get set }
    var router: CreatePostDetailRouterProtocol { get set }
    var model: [AssetModel] { get set }
    
    func viewDidLoad()
    func viewWillAppear()
    func textViewDidEndEditing(_ text: String, tag: Int)
    func textViewDidChange(_ text: String, tag: Int)
    func didTappedBackButton()
    func didTappedShareButton()
}

protocol CreatePostDetailPresenterDelegate: BasePresenterDelegate {
    func prepareNavigationDelegate()
    func prepareNavigationBar()
    func prepareUI()
    func beginUpdates()
}

protocol CreatePostDetailInteractorProtocol: BaseInteractorProtocol {
    var delegate: CreatePostDetailInteractorDelegate? { get set }
    func uploadMultipleImages(_ images: [UIImage]) async
    func uploadVideo(_ url: URL, index: Int) async
    func createPost(_ request: [String: Any]) async
}

protocol CreatePostDetailInteractorDelegate: BaseInteractorDelegate {
    func didUploadMultipleImages(filePaths: [ImageUploadResponseModel])
    func didUploadVideo(filePath: String, index: Int)
    func didCreatePost()
}

protocol CreatePostDetailRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: CreatePostDetailRoutes)
}

enum CreatePostDetailRoutes {
    case back
    case dismiss
}
