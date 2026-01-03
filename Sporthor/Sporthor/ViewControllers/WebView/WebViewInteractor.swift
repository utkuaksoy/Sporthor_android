//
//  WebViewInteractor.swift
//  Sporthor
//
//  Created by derTurke on 15.06.2025.
//
//

import Foundation

final class WebViewInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: WebViewInteractorDelegate? {
        get {
            return self.baseDelegate as? WebViewInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
}

// MARK: - WebViewInteractorProtocol
extension WebViewInteractor: WebViewInteractorProtocol {

}
