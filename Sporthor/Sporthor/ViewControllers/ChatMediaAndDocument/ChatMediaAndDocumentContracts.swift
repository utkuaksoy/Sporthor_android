//
//  ChatMediaAndDocumentContracts.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 15.04.2025.
//

import ChatKit
import Foundation
import UIKit

protocol ChatMediaAndDocumentPresenterProtocol: BasePresenterProtocol {
    var view: ChatMediaAndDocumentPresenterDelegate? { get set }
    var interactor: ChatMediaAndDocumentInteractorProtocol { get set }
    var router: ChatMediaAndDocumentRouterProtocol { get set }
    
    var selectedSegmentIndex: Int { get set }
    var mediaItems: [ChatMessageResponse]? { get }
    var documentItems: [ChatMessageResponse]? { get }
    
    func viewDidLoad()
    func viewWillAppear()
    func didSelectSegment(at index: Int)
    func didSelectItem(at indexPath: IndexPath)
}

protocol ChatMediaAndDocumentPresenterDelegate: BasePresenterDelegate {
    func configureNavigationBar()
    func reloadData()
    func showEmptyView(for type: ChatMediaAndDocumentType)
    func hideEmptyView()
}

protocol ChatMediaAndDocumentInteractorProtocol: BaseInteractorProtocol {
    var delegate: ChatMediaAndDocumentInteractorDelegate? { get set }
    
    func fetchMediaAndDocumentService() async
}

protocol ChatMediaAndDocumentInteractorDelegate: BaseInteractorDelegate {
    func fetchMediaAndDocumentsSuccess(_ response: ChatMediaAndDocumentResponse)
}

protocol ChatMediaAndDocumentRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: ChatMediaAndDocumentRoutes)
}

enum ChatMediaAndDocumentRoutes {
    case openMedia(item: ChatMessageResponse)
    case openDocument(item: ChatMessageResponse)
}

enum ChatMediaAndDocumentType {
    case media
    case document
    
    var emptyTitleText: String {
        switch self {
        case .media:
            return "Henüz medya yok"
        case .document:
            return "Henüz belge yok"
        }
    }
    
}
