//
//  WebViewPresenter.swift
//  Sporthor
//
//  Created by derTurke on 15.06.2025.
//
//

import Foundation
import CommonKit

final class WebViewPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: WebViewPresenterDelegate? {
        get { return self.baseView as? WebViewPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: WebViewInteractorProtocol {
        get { return self.baseInteractor as! WebViewInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: WebViewRouterProtocol {
        get { return self.baseRouter as! WebViewRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: WebViewPresenterDelegate,
         interactor: WebViewInteractorProtocol,
         router: WebViewRouterProtocol,
         title: String,
         url: String,
         isPresent: Bool) {
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
        self.title = title
        self.url = url
        self.isPresent = isPresent
    }
    private var title: String = ""
    var url: String = ""
    private var isPresent: Bool = false
}

// MARK: - WebViewPresenterProtocol
extension WebViewPresenter: WebViewPresenterProtocol {
    func viewDidLoad() {
        view?.prepareNavigationBar()
        view?.didSetTitle(title)
        view?.prepareUI()
        prepareLoadWebView()
    }
    
    private func navigate(_ routes: WebViewRoutes) {
        router.handleRouter(routes)
    }
    
    private func prepareLoadWebView() {
        let url = URL(string: url)
        guard let url else { return }
        let urlRequest = URLRequest(url: url)
        view?.loadWebView(urlRequest: urlRequest)
    }
    
    func didTappedNavigationButton(_ type: BarButtonItemType) {
        switch type {
        case .back:
            navigate(isPresent ? .dismiss : .back)
        default:
            break
        }
    }
    
    func didFail() {
        Task { @MainActor in
            BaseHelper.shared.hideIndicator()
        }
        showAlert(delegate: self,
                  type: .error,
                  message: "Sayfa yüklenirken hata oluştu. Lütfen tekrar deneyiniz.")
    }
}

// MARK: - WebViewInteractorDelegate
extension WebViewPresenter: WebViewInteractorDelegate {}

// MARK: - AlertViewDelegate
extension WebViewPresenter: AlertViewDelegate {
    func didTappedAlertButton(_ tag: Int) {
        navigate(.back)
    }
}
