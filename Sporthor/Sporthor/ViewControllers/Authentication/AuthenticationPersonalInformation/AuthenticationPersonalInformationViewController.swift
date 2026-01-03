//
//  AuthenticationPersonalInformationViewController.swift
//  Sporthor
//
//  Created by derTurke.
//
//

import UIKit
import ComponentKit

final class AuthenticationPersonalInformationViewController: BaseViewController {
    // MARK: - VIPER Variables
    var presenter: AuthenticationPersonalInformationPresenterProtocol {
        get { return self.basePresenter as! AuthenticationPersonalInformationPresenterProtocol }
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
    
    private lazy var agreementLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentStrong900.color,
                            numberOfLines: 0,
                            font: .body04Compact,
                            isUserInteractionEnabled: true,
                            tag: 99)
        label.translatesAutoresizingMaskIntoConstraints = false
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(didTappedAgreementLabel(_:)))
        label.addGestureRecognizer(tapGesture)
        return label
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
        button.heightAnchor.constraint(equalToConstant: 46).isActive = true
        return button
    }()
    
    private lazy var buttonsStackView: CKStackView = {
        let stackView = CKStackView(spacing: 12)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.addArrangedSubviews([agreementLabel, continueButton])
        return stackView
    }()
    
    // MARK: - Members
    
    // MARK: - Lifecycles
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.viewDidLoad()
    }
    
    // MARK: - Custom Methods
}

// MARK: - AuthenticationPersonalInformationPresenterDelegate
extension AuthenticationPersonalInformationViewController: AuthenticationPersonalInformationPresenterDelegate {
    func didSetTitleAndDescriptionText(_ title: String, _ description: String) {
        titleLabel.text = title
        descriptionLabel.text = description
    }
    
    func updateContinueButtonTitle(_ title: String) {
        continueButton.setTitle(title)
    }
    
    func continueButtonEnabled(isEnabled: Bool) {
        continueButton.setEnabled(isEnabled)
    }
    
    func prepareUI() {
        textStackView.addArrangedSubviews([titleLabel, descriptionLabel])
        view.addSubview(textStackView)
        view.addSubview(buttonsStackView)
        view.addSubview(collectionView)
        layoutConstraintActive()
    }
    
    private func layoutConstraintActive() {
        NSLayoutConstraint.activate([
            textStackView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 24),
            textStackView.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 24),
            textStackView.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -24),
            buttonsStackView.leadingAnchor.constraint(equalTo: textStackView.leadingAnchor),
            buttonsStackView.trailingAnchor.constraint(equalTo: textStackView.trailingAnchor),
            buttonsStackView.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: -50),
            collectionView.topAnchor.constraint(equalTo: textStackView.bottomAnchor),
            collectionView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            collectionView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            collectionView.bottomAnchor.constraint(equalTo: buttonsStackView.topAnchor)
        ])
    }
    
    func reloadData() {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.collectionView.reloadData()
        }
    }
    
    func didSetFocusTextField(at indexPath: IndexPath) {
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.2) { [weak self] in
            guard let self else { return }
            guard let nextCell = self.collectionView.cellForItem(at: indexPath) as? TextFieldCollectionViewCell else { return }
            nextCell.textFieldBecomeFirstResponder()
        }
    }
    
    func didSetAgreementText(_ text: String) {
        let attributedString = NSMutableAttributedString(string: text)
        let termsOfUseRange = (text as NSString).range(of: "Kullanım Şartlarını")
        let privacyPolicyRange = (text as NSString).range(of: "Gizlilik Sözleşmesini")
        attributedString.addAttribute(.foregroundColor, value: DesignKitColorName.blue500.color, range: termsOfUseRange)
        
        attributedString.addAttribute(.foregroundColor, value: DesignKitColorName.blue500.color, range: privacyPolicyRange)
        
        agreementLabel.attributedText = attributedString
    }
    
    @objc private func didTappedAgreementLabel(_ gesture: UITapGestureRecognizer) {
        guard let label = gesture.view as? CKLabel else { return }
        label.detectTap(on: "Kullanım Şartlarını",
                        gesture: gesture) { [weak self] in
            guard let self else { return }
            self.presenter.openTermsOfUse()
        }
        
        label.detectTap(on: "Gizlilik Sözleşmesini",
                        gesture: gesture) { [weak self] in
            guard let self else { return }
            self.presenter.openPrivacyPolicy()
        }
    }
}

// MARK: - UICollectionViewDataSource
extension AuthenticationPersonalInformationViewController: UICollectionViewDataSource {
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 3
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        switch section {
        case 0:
            return 2
        default:
            return 1
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = TextFieldCollectionViewCell.dequeue(from: collectionView, at: indexPath)
        switch indexPath.section {
        case 0:
            switch indexPath.item {
            case 0:
                cell.bind(delegate: self,
                          titleText: DesignKitL10n.Authentication.PersonalInformation.name,
                          textFieldText: presenter.name,
                          textFieldPlaceholder: DesignKitL10n.Authentication.PersonalInformation.name,
                          textFieldTag: 0)
            case 1:
                cell.bind(delegate: self,
                          titleText: DesignKitL10n.Authentication.PersonalInformation.surname,
                          textFieldText: presenter.surname,
                          textFieldPlaceholder: DesignKitL10n.Authentication.PersonalInformation.surname,
                          textFieldTag: 1)
            default:
                break
            }
        case 1:
            cell.bind(delegate: self,
                      titleText: DesignKitL10n.Authentication.PersonalInformation.email,
                      textFieldText: presenter.email,
                      textFieldPlaceholder: DesignKitL10n.Authentication.PersonalInformation.email,
                      textFieldKeyboardType: .emailAddress,
                      textFieldMaxLength: 150,
                      textFieldTag: 2)
        case 2:
            cell.bind(delegate: self,
                      titleText: DesignKitL10n.Authentication.PersonalInformation.password,
                      textFieldText: presenter.password,
                      textFieldPlaceholder: DesignKitL10n.Authentication.PersonalInformation.password,
                      textFieldImage: presenter.isSecureText ? Asset.eyeSlash.image : Asset.eye.image,
                      textFieldImageWidth: 18,
                      textFieldImageHeight: 18,
                      textFieldIsSecureText: presenter.isSecureText,
                      textFieldMaxLength: 24,
                      textFieldTag: 3,
                      statusTitleText: presenter.statusModel?.titleText ?? "",
                      statusTitleColor: presenter.statusModel?.titleTextColor ?? .clear,
                      statusTitleFont: presenter.statusModel?.titleTextFont,
                      statusImage: UIImage(named: presenter.statusModel?.image ?? ""),
                      statusImageWidth: presenter.statusModel?.imageWidth ?? 0,
                      statusImageHeight: presenter.statusModel?.imageHeight ?? 0,
                      statusDescriptionText: presenter.statusModel?.descriptionText ?? "",
                      statusDescriptionColor: presenter.statusModel?.descriptionTextColor ?? .clear,
                      statusDescriptionFont: presenter.statusModel?.descriptionTextFont,
                      indexPath: indexPath)
        default:
            break
        }
        return cell
    }
}

// MARK: - UICollectionViewDelegateFlowLayout
extension AuthenticationPersonalInformationViewController: UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        switch indexPath.section{
        case 0:
            switch indexPath.item {
            case 0:
                return CGSize(width: (collectionView.frame.width - 60) / 2 , height: 70)
            case 1:
                return CGSize(width: (collectionView.frame.width - 60) / 2, height: 70)
            default:
                break
            }
        case 1:
            return CGSize(width: collectionView.frame.width - 48, height: 70)
        case 2:
            return CGSize(width: collectionView.frame.width - 48,
                          height: (presenter.statusModel?.descriptionText.isEmpty ?? true) ? 70 : 96)
        default:
            break
        }
        return .zero
    }
    
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, insetForSectionAt section: Int) -> UIEdgeInsets {
        switch section {
        case 0:
            return UIEdgeInsets(top: 0, left: 24, bottom: 24, right: 24)
        case 1:
            return UIEdgeInsets(top: 0, left: 0, bottom: 24, right: 0)
        default:
            return .zero
        }
    }
    
}

// MARK: - TextFieldCollectionViewCellDelegate
extension AuthenticationPersonalInformationViewController: TextFieldCollectionViewCellDelegate {
    func textFieldDidEndEditing(text: String, tag: Int, indexPath: IndexPath?) {
        presenter.textFieldDidEndEditing(text: text, tag: tag)
    }
    
    func textFieldImageTapped(tag: Int, indexPath: IndexPath?) {
        presenter.textFieldImageTapped(tag: tag, indexPath: indexPath)
    }
}

// MARK: - CKButtonDelegate
extension AuthenticationPersonalInformationViewController: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        presenter.didTappedButton(tag: tag)
    }
}
