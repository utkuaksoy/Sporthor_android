//
//  WebViewBuilder.swift
//  Sporthor
//
//  Created by derTurke on 15.06.2025.
//
//

import Foundation

final class WebViewBuilder {
    static func build(title: String,
                      url: String,
                      isPresent: Bool = false) -> WebViewViewController {
        let view = WebViewViewController()
        let interactor = WebViewInteractor()
        let router = WebViewRouter(viewController: view)
        let presenter = WebViewPresenter(view: view, interactor: interactor, router: router, title: title, url: url, isPresent: isPresent)
        view.presenter = presenter
        return view
    }
}
