//
//  BasePresenter.swift
//  Sporthor
//
//  Created by derTurke on 29.01.2025.
//

import Foundation
import ComponentKit

class BasePresenter {
    // MARK: - VIPER Variables
    weak var baseView: BasePresenterDelegate?
    var baseInteractor: BaseInteractorProtocol?
    var baseRouter: BaseRouterProtocol?
}

// MARK: - BasePresenterProtocol
extension BasePresenter: BasePresenterProtocol {
    func baseViewDidLoad() {
        baseView?.didSetBackgroundColor(.white)
    }
    
    func showAlert(delegate: AlertViewDelegate? = nil,
                   type: AlertType,
                   title: String = "",
                   message: String,
                   buttonTitle: String = DesignKitL10n.alertButtonOK,
                   tag: Int = 0) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.baseRouter?.showAlert(delegate: delegate,
                                       type: type,
                                       title: title,
                                       message: message,
                                       buttonTitle: buttonTitle,
                                       tag: tag)
        }
    }
    
    func showCKDefaultAlert(
        delegate: CKDefaultAlertDelegate?,
        title: String = "",
        message: String,
        okTitle: String = "Tamam",
        cancelTitle: String? = nil
    ) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.baseRouter?.showCKDefaultAlert(delegate: delegate,
                                                title: title,
                                                message: message,
                                                okTitle: okTitle,
                                                cancelTitle: cancelTitle)
        }
    }
}

// MARK: - BaseInteractorDelegate
extension BasePresenter: BaseInteractorDelegate {
    func didFailure(_ error: NetworkError) {
        switch error {
        case .error(let baseError):
            showAlert(type: .error, message: baseError.message ?? DesignKitL10n.generalError)
        case .unauthorized:
            baseInteractor?.baseLogout()
        default:
            showAlert(type: .error,
                      message: DesignKitL10n.generalError)
        }
    }
    
    func didBaseLogout() {
        baseRouter?.baseLogout()
    }
}
