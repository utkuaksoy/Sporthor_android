//
//  PersonsPermissionViewController.swift
//  Sporthor
//
//  Created by derTurke on 23.02.2025.
//
//

import UIKit
import ComponentKit

final class PersonsPermissionViewController: BaseViewController {
    // MARK: - VIPER Variables
    var presenter: PersonsPermissionPresenterProtocol {
        get { return self.basePresenter as! PersonsPermissionPresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - UI Elements
    private lazy var gradientView: CKGradientView = {
        let gradientView = CKGradientView(colors: [DesignKitColorName.successLighter100.color, .white])
        gradientView.translatesAutoresizingMaskIntoConstraints = false
        return gradientView
    }()
    
    private lazy var closeButton: CKButton = {
        let button = CKButton(delegate: self,
                              image: Asset.close.image,
                              tag: 0)
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()
    
    private lazy var textStackView: CKStackView = {
        let stackView = CKStackView(spacing: 8)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var titleLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentStrong900.color, numberOfLines: 0, font: .heading04)
        return label
    }()
    
    private lazy var descriptionLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentSub800.color, numberOfLines: 0, font: .body04Compact)
        return label
    }()
    
    private lazy var continueButton: CKButton = {
        let button = CKButton(delegate: self,
                              title: DesignKitL10n.PersonsPermission.buttonTitle,
                              titleColor: .white,
                              buttonBackgroundColor: DesignKitColorName.contentStrong900.color,
                              cornerRadius: 23,
                              font: .bold03Compact,
                              tag: 1)
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()
    
    // MARK: - Members
    
    // MARK: - Lifecycles
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.viewDidLoad()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        navigationController?.setNavigationBarHidden(true, animated: true)
    }
    
    // MARK: - Custom Methods
}

// MARK: - PersonsPermissionPresenterDelegate
extension PersonsPermissionViewController: PersonsPermissionPresenterDelegate {
    func didSetTitleAndDescriptionText(_ title: String, _ description: String) {
        titleLabel.text = title
        descriptionLabel.text = description
    }
    
    func prepareUI() {
        view.addSubview(gradientView)
        gradientView.addSubview(closeButton)
        textStackView.addArrangedSubviews([titleLabel, descriptionLabel])
        gradientView.addSubview(textStackView)
        gradientView.addSubview(continueButton)
        
        NSLayoutConstraint.activate([
            gradientView.topAnchor.constraint(equalTo: view.topAnchor),
            gradientView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            gradientView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            gradientView.bottomAnchor.constraint(equalTo: view.bottomAnchor),
            
            closeButton.topAnchor.constraint(equalTo: gradientView.safeAreaLayoutGuide.topAnchor, constant: 20),
            closeButton.trailingAnchor.constraint(equalTo: gradientView.trailingAnchor, constant: -24),
            closeButton.heightAnchor.constraint(equalToConstant: 32),
            closeButton.widthAnchor.constraint(equalToConstant: 32),
            
            textStackView.topAnchor.constraint(equalTo: closeButton.bottomAnchor, constant: 25),
            textStackView.leadingAnchor.constraint(equalTo: gradientView.leadingAnchor, constant: 24),
            textStackView.trailingAnchor.constraint(equalTo: gradientView.trailingAnchor, constant: -24),
            
            continueButton.bottomAnchor.constraint(equalTo: gradientView.safeAreaLayoutGuide.bottomAnchor, constant: -50),
            continueButton.leadingAnchor.constraint(equalTo: gradientView.leadingAnchor, constant: 24),
            continueButton.trailingAnchor.constraint(equalTo: gradientView.trailingAnchor, constant: -24),
            continueButton.heightAnchor.constraint(equalToConstant: 46)
        ])
    }
}

// MARK: - CKButtonDelegate
extension PersonsPermissionViewController: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        presenter.didTappedButton(tag)
    }
}
