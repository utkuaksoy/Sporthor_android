//
//  OnboardingViewController.swift
//  Sporthor
//
//  Created by derTurke on 6.02.2025.
//
//

import UIKit
import ComponentKit

final class OnboardingViewController: BaseViewController {
    // MARK: - VIPER Variables
    var presenter: OnboardingPresenterProtocol {
        get { return self.basePresenter as! OnboardingPresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - UI Elements
    private lazy var welcomeBannerImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.image = Asset.welcomeBanner.image
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.clipsToBounds = true
        imageView.isHidden = true
        return imageView
    }()
    
    private lazy var textStackView: CKStackView = {
        let stackView = CKStackView(alignment: .center, spacing: 16)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    private lazy var titleLabel: CKLabel = {
        let label = CKLabel(
            text: "1000".localizedText,
            textColor: .white,
            numberOfLines: 0,
            font: .heading01)
        return label
    }()
    
    private lazy var descriptionLabel: CKLabel = {
        let label = CKLabel(
            text: DesignKitL10n.Onboarding.description,
            textColor: DesignKitColorName.contentWeak200.color,
            numberOfLines: 0,
            font: .body03Compact)
        return label
    }()
    
    private lazy var buttonStackView: CKStackView = {
        let stackView = CKStackView(spacing: 20)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var registerButton: CKButton = {
        let button = CKButton(
            delegate: self,
            title: DesignKitL10n.Onboarding.register,
            titleColor: DesignKitColorName.contentStrong900.color,
            buttonBackgroundColor: DesignKitColorName.backgroundPrimaryGreen.color,
            cornerRadius: 23,
            font: .bold03Compact,
            tag: 0)
        button.heightAnchor.constraint(equalToConstant: 46).isActive = true
        return button
    }()
    
    private lazy var loginButton: CKButton = {
        let button = CKButton(
            delegate: self,
            title: DesignKitL10n.Onboarding.login,
            titleColor: DesignKitColorName.contentStrong900.color,
            buttonBackgroundColor: .white,
            cornerRadius: 23,
            borderWidth: 1,
            borderColor: DesignKitColorName.borderStrong900.color,
            font: .bold03Compact,
            tag: 1)
        button.heightAnchor.constraint(equalToConstant: 46).isActive = true
        return button
    }()
    
    private lazy var discoverButton: CKButton = {
        let button = CKButton(
            delegate: self,
            title: DesignKitL10n.Onboarding.discoverTheApp,
            titleColor: .white,
            font: .bold03Compact,
            tag: 2)
        button.isHidden = true
        button.heightAnchor.constraint(equalToConstant: 46).isActive = true
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
        presenter.viewWillAppear()
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        presenter.viewWillDisappear()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        welcomeBannerImageView.isHidden = false
    }
    
    // MARK: - Custom Methods
}

// MARK: - OnboardingPresenterDelegate
extension OnboardingViewController: OnboardingPresenterDelegate {
    func didSetBackgroundImage(image: String) {
        let imageView = UIImageView(image: UIImage(named: image))
        imageView.frame = view.bounds
        imageView.contentMode = .scaleAspectFill
        view.addSubview(imageView)
        view.sendSubviewToBack(imageView)
    }
    
    func updateNavigationBarHidden(_ isHidden: Bool) {
        navigationController?.isNavigationBarHidden = isHidden
    }
    
    func prepareUI() {
        view.addSubview(welcomeBannerImageView)
        textStackView.addArrangedSubviews([titleLabel, descriptionLabel])
        view.addSubview(textStackView)
        
        buttonStackView.addArrangedSubviews([registerButton, loginButton, discoverButton])
        view.addSubview(buttonStackView)
        
        NSLayoutConstraint.activate([
            
            textStackView.centerYAnchor.constraint(equalTo: view.centerYAnchor),
            textStackView.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 86),
            textStackView.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -86),
            
            buttonStackView.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: -50),
            buttonStackView.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 24),
            buttonStackView.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -24),
            
            welcomeBannerImageView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 50),
            welcomeBannerImageView.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: -60),
            welcomeBannerImageView.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: 60),
            welcomeBannerImageView.bottomAnchor.constraint(equalTo: buttonStackView.topAnchor, constant: -58)
        ])
    }
}

extension OnboardingViewController: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        presenter.didTappedButton(tag: tag)
    }
}
