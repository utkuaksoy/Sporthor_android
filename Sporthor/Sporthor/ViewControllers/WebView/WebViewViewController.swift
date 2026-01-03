//
//  WebViewViewController.swift
//  Sporthor
//
//  Created by derTurke on 15.06.2025.
//
//

import UIKit
import WebKit
import CommonKit

final class WebViewViewController: BaseViewController {
    // MARK: - VIPER Variables
    var presenter: WebViewPresenterProtocol {
        get { return self.basePresenter as! WebViewPresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - UI Elements
    private lazy var webView: WKWebView = {
        let webView = WKWebView()
        webView.navigationDelegate = self
        webView.uiDelegate = self
        webView.translatesAutoresizingMaskIntoConstraints = false
        return webView
    }()
    
    // MARK: - Members
    
    // MARK: - Lifecycles
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.viewDidLoad()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        navigationController?.navigationItem.setHidesBackButton(true, animated: false)
        if let navCon = navigationController as? CustomNavigationController {
            navCon.customDelegate = self
        }
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        navigationController?.navigationItem.setHidesBackButton(false, animated: false)
    }
    
    // MARK: - Custom Methods
}

// MARK: - WebViewPresenterDelegate
extension WebViewViewController: WebViewPresenterDelegate {
    func prepareNavigationBar() {
        if let navCon = navigationController as? CustomNavigationController {
            navCon.isBackChevronLeft = true
            navCon.navigationBar.titleTextAttributes = [
                .foregroundColor: UIColor.black,
                .font: UIFont.bold03Compact
            ]
        }
    }
    
    func prepareUI() {
        view.addSubview(webView)
        NSLayoutConstraint.activate([
            webView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor),
            webView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            webView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            webView.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor)
        ])
    }
    
    func loadWebView(urlRequest: URLRequest) {
        webView.load(urlRequest)
    }
}

// MARK: - WKNavigationDelegate
extension WebViewViewController: WKNavigationDelegate {
    func webView(_ webView: WKWebView, didStartProvisionalNavigation navigation: WKNavigation!) {
        Task { @MainActor in
            BaseHelper.shared.showIndicator()
        }
    }

    func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
        Task { @MainActor in
            BaseHelper.shared.hideIndicator()
        }
    }

    func webView(_ webView: WKWebView, didFail navigation: WKNavigation!, withError error: Error) {
        presenter.didFail()
    }
}

// MARK: - WKUIDelegate
extension WebViewViewController: WKUIDelegate {}

// MARK: - CustomNavigationControllerDelegate
extension WebViewViewController: CustomNavigationControllerDelegate {
    func didTapButton(type: BarButtonItemType) {
        presenter.didTappedNavigationButton(type)
    }
}
