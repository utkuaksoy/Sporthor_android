//
//  ChatMediaAndDocumentPresenter.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 15.04.2025.
//

import ChatKit
import Foundation

final class ChatMediaAndDocumentPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: ChatMediaAndDocumentPresenterDelegate? {
        get { return self.baseView as? ChatMediaAndDocumentPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: ChatMediaAndDocumentInteractorProtocol {
        get { return self.baseInteractor as! ChatMediaAndDocumentInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: ChatMediaAndDocumentRouterProtocol {
        get { return self.baseRouter as! ChatMediaAndDocumentRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Properties
    var selectedSegmentIndex: Int = 0 {
        didSet {
            handleSegmentChange()
        }
    }
    
    private(set) var mediaItems: [ChatMessageResponse]?
    private(set) var documentItems: [ChatMessageResponse]?
    
    // MARK: - Initialize
    init(view: ChatMediaAndDocumentPresenterDelegate,
         interactor: ChatMediaAndDocumentInteractorProtocol,
         router: ChatMediaAndDocumentRouterProtocol) {
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
    }
}

// MARK: - ChatMediaAndDocumentPresenterProtocol
extension ChatMediaAndDocumentPresenter: ChatMediaAndDocumentPresenterProtocol {
    func viewDidLoad() {
        view?.configureNavigationBar()        
        Task {
            await interactor.fetchMediaAndDocumentService()
        }
    }
    
    func viewWillAppear() {
        handleSegmentChange()
    }
    
    func didSelectSegment(at index: Int) {
        selectedSegmentIndex = index
    }
    
    func didSelectItem(at indexPath: IndexPath) {
        if selectedSegmentIndex == 0 {
            if let item = mediaItems?[indexPath.item] {
                router.handleRouter(.openMedia(item: item))
            }
        } else {
           
        }
    }
}

// MARK: - ChatMediaAndDocumentInteractorDelegate
extension ChatMediaAndDocumentPresenter: ChatMediaAndDocumentInteractorDelegate {
    func fetchMediaAndDocumentsSuccess(_ response: ChatMediaAndDocumentResponse) {
        self.mediaItems = response.medias
        self.documentItems = response.files
        DispatchQueue.main.async { [weak self] in
            if self?.selectedSegmentIndex == 0 {
                if let items = self?.mediaItems, !items.isEmpty {
                    self?.view?.hideEmptyView()
                    self?.view?.reloadData()
                } else {
                    self?.view?.showEmptyView(for: .media)
                }
            } else {
                if let items = self?.documentItems, !items.isEmpty {
                    self?.view?.hideEmptyView()
                    self?.view?.reloadData()
                } else {
                    self?.view?.showEmptyView(for: .document)
                }
            }
        }
        
    }
}

// MARK: - Private Methods
private extension ChatMediaAndDocumentPresenter {
    func handleSegmentChange() {
        if selectedSegmentIndex == 0 {
            if let items = mediaItems {
                if items.isEmpty {
                    view?.showEmptyView(for: .media)
                } else {
                    view?.hideEmptyView()
                }
            }
        } else {
            if let items = documentItems {
                if items.isEmpty {
                    view?.showEmptyView(for: .document)
                } else {
                    view?.hideEmptyView()
                }
            }
        }
        view?.reloadData()
    }
} 
