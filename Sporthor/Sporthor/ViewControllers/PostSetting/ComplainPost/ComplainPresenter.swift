//
//  ComplainPresenter.swift
//  Sporthor
//
//  Created by derTurke on 6.05.2025.
//
//

import Foundation

final class ComplainPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: ComplainPresenterDelegate? {
        get { return self.baseView as? ComplainPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: ComplainInteractorProtocol {
        get { return self.baseInteractor as! ComplainInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: ComplainRouterProtocol {
        get { return self.baseRouter as! ComplainRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: ComplainPresenterDelegate,
         interactor: ComplainInteractorProtocol,
         router: ComplainRouterProtocol,
         delegate: ComplainDelegate?,
         model: Post) {
        self.complainDelegate = delegate
        self.model = model
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
    }
    private weak var complainDelegate: ComplainDelegate?
    private let model: Post
    private var reason: String = ""
}

// MARK: - ComplainPresenterProtocol
extension ComplainPresenter: ComplainPresenterProtocol {
    func viewDidLoad() {
        view?.prepareUI()
    }
    
    private func navigate(_ routes: ComplainRoutes) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.router.handleRouter(routes)
        }
    }
    
    func reasonDidChange(_ text: String) {
        reason = text
    }
    
    func didTappedButton(_ tag: Int) {
        switch tag {
        case 0:
            navigate(.dismiss(delegate: nil))
        case 1:
            reportPost()
        default:
            break
        }
    }
    
    private func reportPost() {
        var request: [String: Any] = ["postId": model.id]
        if !reason.isEmpty {
            request["reason"] = reason
        }
        Task { @MainActor in
            await interactor.reportPost(request)
        }
    }
}

// MARK: - ComplainInteractorDelegate
extension ComplainPresenter: ComplainInteractorDelegate {
    func didReportPost() {
        navigate(.dismiss(delegate: complainDelegate))
    }
}
