//
//  Contracts.swift
//  Sporthor
//
//  Created by derTurke on 29.01.2025.
//

import UIKit
import ComponentKit

protocol BasePresenterProtocol {
    var baseView: BasePresenterDelegate? { get set }
    var baseInteractor: BaseInteractorProtocol? { get set }
    var baseRouter: BaseRouterProtocol? { get set }
    
    func baseViewDidLoad()
    func showAlert(delegate: AlertViewDelegate?,
                   type: AlertType,
                   title: String,
                   message: String,
                   buttonTitle: String,
                   tag: Int)
}

protocol BasePresenterDelegate: AnyObject {
    func didSetBackgroundColor(_ color: UIColor)
    func didSetTitle(_ title: String)
}

protocol BaseInteractorProtocol {
    var baseDelegate: BaseInteractorDelegate? { get set }
    func baseLogout()
}

protocol BaseInteractorDelegate: AnyObject {
    func didFailure(_ error: NetworkError)
    func didBaseLogout()
}

protocol BaseRouterProtocol {
    func showAlert(delegate: AlertViewDelegate?,
                   type: AlertType,
                   title: String,
                   message: String,
                   buttonTitle: String,
                   tag: Int)
    func baseLogout()
    func showCKDefaultAlert(
        delegate: CKDefaultAlertDelegate?,
        title: String,
        message: String,
        okTitle: String,
        cancelTitle: String?
    )
}


