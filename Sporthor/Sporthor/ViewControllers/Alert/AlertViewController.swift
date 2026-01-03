//
//  AlertViewController.swift
//  Sporthor
//
//  Created by derTurke on 28.02.2025.
//

import UIKit
import ComponentKit

final class AlertViewController: BaseViewController {
    // MARK: - UI Elements
    private lazy var containerView: UIView = {
        let view = UIView()
        view.backgroundColor = .white
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var iconImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()
    
    private lazy var stackView: CKStackView = {
        let stackView = CKStackView(alignment: .center, spacing: 8)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var titleLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentStrong900.color,
                            numberOfLines: 0,
                            textAlignment: .center,
                            font: .heading06)
        return label
    }()
    
    private lazy var messageLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentSub800.color,
                            numberOfLines: 0,
                            textAlignment: .center,
                            font: .body04Compact)
        return label
    }()
    
    private lazy var button: CKButton = {
        let button = CKButton(delegate: self,
                              titleColor: DesignKitColorName.contentStrong900.color,
                              buttonBackgroundColor: DesignKitColorName.backgroundPrimaryGreen.color,
                              cornerRadius: 23,
                              font: .bold03Compact)
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()
    
    // MARK: - Members
    weak var delegate: AlertViewDelegate?
    var type: AlertType?
    var titleMessage: String?
    var message: String?
    var buttonTitle: String?
    var tag: Int?
    
    // MARK: - Lifecycles
    override func viewDidLoad() {
        super.viewDidLoad()
        prepareUI()
    }
    
    // MARK: - Custom Methods
    private func prepareUI() {
        view.backgroundColor = .black.withAlphaComponent(0.3)
        containerView.layer.cornerRadius = 16
        containerView.layer.maskedCorners = [.layerMinXMinYCorner, .layerMaxXMinYCorner]
        switch type {
        case .error:
            iconImageView.image = Asset.error.image
        case .success:
            iconImageView.image = Asset.success.image
        case .warning:
            iconImageView.image = Asset.warning.image
        default:
            break
        }
        titleLabel.text = titleMessage
        messageLabel.text = message
        button.setTitle(buttonTitle ?? "")
        button.setTag(tag ?? 0)
        
        prepareLayout()
    }
    
    private func prepareLayout() {
        view.addSubview(containerView)
        containerView.addSubview(iconImageView)
        stackView.addArrangedSubviews([titleLabel, messageLabel])
        containerView.addSubview(stackView)
        containerView.addSubview(button)
        
        NSLayoutConstraint.activate([
            containerView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            containerView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            containerView.bottomAnchor.constraint(equalTo: view.bottomAnchor),
            
            iconImageView.topAnchor.constraint(equalTo: containerView.topAnchor, constant: 20),
            iconImageView.centerXAnchor.constraint(equalTo: containerView.centerXAnchor),
            iconImageView.widthAnchor.constraint(equalToConstant: 74),
            iconImageView.heightAnchor.constraint(equalToConstant: 74),
            
            stackView.topAnchor.constraint(equalTo: iconImageView.bottomAnchor, constant: 24),
            stackView.leadingAnchor.constraint(equalTo: containerView.leadingAnchor, constant: 24),
            stackView.trailingAnchor.constraint(equalTo: containerView.trailingAnchor, constant: -24),
            
            button.topAnchor.constraint(equalTo: stackView.bottomAnchor, constant: 50),
            button.leadingAnchor.constraint(equalTo: containerView.leadingAnchor, constant: 24),
            button.trailingAnchor.constraint(equalTo: containerView.trailingAnchor, constant: -24),
            button.bottomAnchor.constraint(equalTo: containerView.safeAreaLayoutGuide.bottomAnchor, constant: -35),
            button.heightAnchor.constraint(equalToConstant: 46)
        ])
    }
}

extension AlertViewController: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        dismiss(animated: true) { [weak self] in
            guard let self, let delegate else { return }
            delegate.didTappedAlertButton(tag)
        }
    }
}
