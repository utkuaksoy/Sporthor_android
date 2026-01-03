//
//  AlertViewBuilder.swift
//  Sporthor
//
//  Created by derTurke on 28.02.2025.
//

import Foundation

// MARK: - AlertType
enum AlertType {
    case error
    case success
    case warning
}

// MARK: - AlertViewDelegate
protocol AlertViewDelegate: AnyObject {
    func didTappedAlertButton(_ tag: Int)
}

final class AlertViewBuilder {
    weak var delegate: AlertViewDelegate?
    var type: AlertType?
    var title: String?
    var message: String?
    var buttonTitle: String?
    var tag: Int?
    
    init(delegate: AlertViewDelegate? = nil,
         type: AlertType? = nil,
         title: String? = nil,
         message: String? = nil,
         buttonTitle: String? = nil,
         tag: Int? = nil) {
        self.delegate = delegate
        self.type = type
        self.title = title
        self.message = message
        self.buttonTitle = buttonTitle
        self.tag = tag
    }
    
    func make() -> AlertViewController {
        let vc = AlertViewController()
        vc.delegate = delegate
        vc.type = type
        vc.titleMessage = title
        vc.message = message
        vc.buttonTitle = buttonTitle
        vc.tag = tag
        return vc
    }
    
    deinit {
        delegate = nil
        type = nil
        title = nil
        message = nil
        buttonTitle = nil
        tag = nil
    }
}
