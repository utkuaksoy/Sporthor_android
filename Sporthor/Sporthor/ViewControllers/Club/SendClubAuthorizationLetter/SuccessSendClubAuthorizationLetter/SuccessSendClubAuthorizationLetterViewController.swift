//
//  SuccessSendClubAuthorizationLetterViewController.swift
//  Sporthor
//
//  Created by derTurke on 20.05.2025.
//
//

import UIKit
import ComponentKit

final class SuccessSendClubAuthorizationLetterViewController: BaseViewController {
    // MARK: - VIPER Variables
    var presenter: SuccessSendClubAuthorizationLetterPresenterProtocol {
        get { return self.basePresenter as! SuccessSendClubAuthorizationLetterPresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - UI Elements
    private lazy var leftBackgroundImageView: UIImageView = {
        let imageView = UIImageView(image: Asset.backgroundWhiteEllipse.image)
        imageView.contentMode = .scaleAspectFit
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.heightAnchor.constraint(equalToConstant: 277).isActive = true
        imageView.widthAnchor.constraint(equalToConstant: 277).isActive = true
        return imageView
    }()
    
    private lazy var rightBackgroundImageView: UIImageView = {
        let imageView = UIImageView(image: Asset.backgroundLightGreenEllipse.image)
        imageView.contentMode = .scaleAspectFit
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.heightAnchor.constraint(equalToConstant: 277).isActive = true
        imageView.widthAnchor.constraint(equalToConstant: 277).isActive = true
        return imageView
    }()
    
    private lazy var successDocumentIconImageView: UIImageView = {
        let imageView = UIImageView(image: Asset.documentSuccessIcon.image)
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.heightAnchor.constraint(equalToConstant: 140).isActive = true
        imageView.widthAnchor.constraint(equalToConstant: 140).isActive = true
        return imageView
    }()
    
    private lazy var successHeaderLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentStrong900.color,
                            numberOfLines: 0,
                            textAlignment: .center,
                            font: .heading05)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var successDescriptionLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentSub800.color,
                            numberOfLines: 0,
                            textAlignment: .center,
                            font: .body04Compact)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var continueButton: CKButton = {
        let button = CKButton(delegate: self,
                              titleColor: DesignKitColorName.contentStrong900.color,
                              buttonBackgroundColor: DesignKitColorName.backgroundPrimaryGreen.color,
                              cornerRadius: 23,
                              font: .bold03Compact,
                              tag: 0)
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
        navigationController?.setNavigationBarHidden(true, animated: true)
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        navigationController?.setNavigationBarHidden(false, animated: true)
    }
    
    // MARK: - Custom Methods
}

// MARK: - SuccessSendClubAuthorizationLetterPresenterDelegate
extension SuccessSendClubAuthorizationLetterViewController: SuccessSendClubAuthorizationLetterPresenterDelegate {
    func prepareSuccessHeaderAndDescription(header: String, description: String) {
        successHeaderLabel.text = header
        successDescriptionLabel.text = description
    }
    
    func prepareUI() {
        [leftBackgroundImageView,
         rightBackgroundImageView,
         successDocumentIconImageView,
         successHeaderLabel,
         successDescriptionLabel,
         continueButton].forEach {
            $0.translatesAutoresizingMaskIntoConstraints = false
            view.addSubview($0)
        }
        
        NSLayoutConstraint.activate([
            leftBackgroundImageView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor),
            leftBackgroundImageView.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 8),
            
            rightBackgroundImageView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor),
            rightBackgroundImageView.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -8),
            
            successDocumentIconImageView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 50),
            successDocumentIconImageView.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            
            successHeaderLabel.topAnchor.constraint(equalTo: successDocumentIconImageView.bottomAnchor, constant: 2),
            successHeaderLabel.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 24),
            successHeaderLabel.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -24),
            
            successDescriptionLabel.topAnchor.constraint(equalTo: successHeaderLabel.bottomAnchor, constant: 16),
            successDescriptionLabel.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 24),
            successDescriptionLabel.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -24),
            
            continueButton.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: -32),
            continueButton.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 24),
            continueButton.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -24)
        ])
    }
    
    func prepareContinueButton(_ text: String) {
        continueButton.setTitle(text)
    }
}

// MARK: - CKButtonDelegate
extension SuccessSendClubAuthorizationLetterViewController: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        presenter.didTappedHomeButton()
    }
}
