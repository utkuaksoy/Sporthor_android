//
//  AuthenticationLoginUsernameViewController.swift
//  Sporthor
//
//  Created by derTurke on 19.02.2025.
//
//

import UIKit
import ComponentKit

final class AuthenticationLoginUsernameViewController: BaseViewController {
    // MARK: - VIPER Variables
    var presenter: AuthenticationLoginUsernamePresenterProtocol {
        get { return self.basePresenter as! AuthenticationLoginUsernamePresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - UI Elements
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
    
    private lazy var collectionView: UICollectionView = {
        let collectionView = UICollectionView(frame: .zero,
                                              collectionViewLayout: UICollectionViewFlowLayout())
        collectionView.delegate = self
        collectionView.dataSource = self
        collectionView.translatesAutoresizingMaskIntoConstraints = false
        collectionView.contentInset = UIEdgeInsets(top: 32, left: 0, bottom: 0, right: 0)
        collectionView.backgroundColor = .clear
        return collectionView
    }()
    
    private lazy var ckHorizontalTitleButtonView: CKHorizontalTitleButtonView = {
        let ckHorizontalTitleButtonView = CKHorizontalTitleButtonView(
            delegate: self,
            labelText: DesignKitL10n.Authentication.Login.notAMemberYet,
            labelTextColor: DesignKitColorName.contentSub800.color,
            buttonTitle: DesignKitL10n.Authentication.LoginUsername.register,
            buttonTitleColor: DesignKitColorName.contentStrong900.color,
            buttonFont: .bold04Compact)
        ckHorizontalTitleButtonView.translatesAutoresizingMaskIntoConstraints = false
        return ckHorizontalTitleButtonView
    }()
    
    private lazy var continueButton: CKButton = {
        let button = CKButton(
            delegate: self,
            titleColor: DesignKitColorName.contentStrong900.color,
            buttonBackgroundColor: DesignKitColorName.backgroundPrimaryGreen.color,
            cornerRadius: 23,
            disabledTextColor: DesignKitColorName.contentSoft600.color,
            disabledBackgroundColor: DesignKitColorName.backgroundSub300.color,
            font: .bold03Compact,
            isEnabled: false,
            tag: 0)
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()
    
    // MARK: - Members
    
    // MARK: - Lifecycles
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.viewDidLoad()
    }
    
    // MARK: - Custom Methods
}

// MARK: - AuthenticationLoginUsernamePresenterDelegate
extension AuthenticationLoginUsernameViewController: AuthenticationLoginUsernamePresenterDelegate {
    func didSetTitleAndDescription(title: String, description: String) {
        titleLabel.text = title
        descriptionLabel.text = description
    }
    
    func didSetContinueButtonTitle(_ title: String) {
        continueButton.setTitle(title)
    }
    
    func prepareUI() {
        textStackView.addArrangedSubviews([titleLabel, descriptionLabel])
        view.addSubview(textStackView)
        view.addSubview(collectionView)
        view.addSubview(continueButton)
        view.addSubview(ckHorizontalTitleButtonView)
        
        NSLayoutConstraint.activate([
            textStackView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 24),
            textStackView.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 24),
            textStackView.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -24),
            
            continueButton.leadingAnchor.constraint(equalTo: textStackView.leadingAnchor),
            continueButton.trailingAnchor.constraint(equalTo: textStackView.trailingAnchor),
            continueButton.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: -50),
            continueButton.heightAnchor.constraint(equalToConstant: 46),
            
            ckHorizontalTitleButtonView.leadingAnchor.constraint(equalTo: textStackView.leadingAnchor),
            ckHorizontalTitleButtonView.trailingAnchor.constraint(equalTo: textStackView.trailingAnchor),
            ckHorizontalTitleButtonView.bottomAnchor.constraint(equalTo: continueButton.topAnchor, constant: -32),
            ckHorizontalTitleButtonView.heightAnchor.constraint(equalToConstant: 18),
            
            collectionView.topAnchor.constraint(equalTo: textStackView.bottomAnchor),
            collectionView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            collectionView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            collectionView.bottomAnchor.constraint(equalTo: ckHorizontalTitleButtonView.topAnchor)
        ])
    }
    
    func reloadData() {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.collectionView.reloadData()
        }
    }
    
    func updateContinueButtonEnabled(_ isEnabled: Bool) {
        continueButton.setEnabled(isEnabled)
    }
    
    func didSetFocusTextField(at indexPath: IndexPath) {
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.1) { [weak self] in
            guard let self,
                  let cell = self.collectionView.cellForItem(at: indexPath) as? TextFieldCollectionViewCell else { return }
            cell.textFieldBecomeFirstResponder()
        }
    }
}

// MARK: - UICollectionViewDataSource
extension AuthenticationLoginUsernameViewController: UICollectionViewDataSource {
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        3
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return 1
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        switch indexPath.section {
        case 0:
            let cell = TextFieldCollectionViewCell.dequeue(from: collectionView, at: indexPath)
            cell.bind(delegate: self,
                      titleText: DesignKitL10n.Authentication.Username.title,
                      textFieldText: presenter.username,
                      textFieldPlaceholder: DesignKitL10n.Authentication.Username.title,
                      textFieldCapitalizationType: .none,
                      textFieldMaxLength: 15,
                      textFieldTag: 0)
            return cell
        case 1:
            let cell = TextFieldCollectionViewCell.dequeue(from: collectionView, at: indexPath)
            cell.bind(delegate: self,
                      titleText: DesignKitL10n.Authentication.PersonalInformation.password,
                      textFieldText: presenter.password,
                      textFieldPlaceholder: DesignKitL10n.Authentication.PersonalInformation.password,
                      textFieldImage: presenter.isSecureText ? Asset.eyeSlash.image : Asset.eye.image,
                      textFieldImageWidth: 18,
                      textFieldImageHeight: 18,
                      textFieldIsSecureText: presenter.isSecureText,
                      textFieldMaxLength: 24,
                      textFieldTag: 1,
                      indexPath: indexPath)
            return cell
        case 2:
            let cell = ButtonCollectionViewCell.dequeue(from: collectionView, at: indexPath)
            cell.bind(delegate: self,
                      title: DesignKitL10n.Authentication.LoginUsername.forgotPassword,
                      titleColor: DesignKitColorName.contentSoft600.color,
                      alignment: .right,
                      font: .body04Compact,
                      isUnderLine: true)
            return cell
        default:
            return UICollectionViewCell.dequeue(from: collectionView, at: indexPath)
        }
    }
}

// MARK: - UICollectionViewDelegateFlowLayout
extension AuthenticationLoginUsernameViewController: UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        guard indexPath.section != 2 else {
            return CGSize(width: collectionView.frame.width, height: 14)
        }
        return CGSize(width: collectionView.frame.width - 48, height: 70)
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, insetForSectionAt section: Int) -> UIEdgeInsets {
        switch section {
        case 1:
            return UIEdgeInsets(top: 24, left: 0, bottom: 0, right: 0)
        case 2:
            return UIEdgeInsets(top: 18, left: 0, bottom: 0, right: 0)
        default:
            return .zero
        }
    }
}

// MARK: - TextFieldCollectionViewCellDelegate
extension AuthenticationLoginUsernameViewController: TextFieldCollectionViewCellDelegate {
    func textFieldImageTapped(tag: Int, indexPath: IndexPath?) {
        presenter.didTappedTextFieldImage(tag: tag, indexPath: indexPath)
    }
    
    func textFieldDidEndEditing(text: String, tag: Int, indexPath: IndexPath?) {
        presenter.didTextFieldEndEditing(text: text, tag: tag)
    }
}

// MARK: - ButtonCollectionViewDelegate
extension AuthenticationLoginUsernameViewController: ButtonCollectionViewDelegate {
    func didTappedButton(tag: Int, indexPath: IndexPath?) {
        presenter.didTappedCellButton(tag: tag)
    }
}

// MARK: - CKButtonDelegate
extension AuthenticationLoginUsernameViewController: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        presenter.didTappedContinueButton()
    }
}

extension AuthenticationLoginUsernameViewController: CKHorizontalTitleButtonViewDelegate {
    func ckHorizontalButtonClicked(tag: Int) {
        presenter.didTappedCKHorizontalButton(tag: tag)
    }
}
