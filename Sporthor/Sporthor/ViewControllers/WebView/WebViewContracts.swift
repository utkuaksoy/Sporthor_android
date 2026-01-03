//
//  WebViewContracts.swift
//  Sporthor
//
//  Created by derTurke on 15.06.2025.
//
//

import Foundation

protocol WebViewPresenterProtocol: BasePresenterProtocol {
    var view: WebViewPresenterDelegate? { get set }
    var interactor: WebViewInteractorProtocol { get set }
    var router: WebViewRouterProtocol { get set }
    
    func viewDidLoad()
    func didTappedNavigationButton(_ type: BarButtonItemType)
    func didFail()
}

protocol WebViewPresenterDelegate: BasePresenterDelegate {
    func prepareNavigationBar()
    func prepareUI()
    func loadWebView(urlRequest: URLRequest)
}

protocol WebViewInteractorProtocol: BaseInteractorProtocol {
    var delegate: WebViewInteractorDelegate? { get set }
}

protocol WebViewInteractorDelegate: BaseInteractorDelegate {
}

protocol WebViewRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: WebViewRoutes)
}

enum WebViewRoutes {
    case back
    case dismiss
}
