//
//  AuthenticationPhoneViewController.swift
//  Sporthor
//
//  Created by derTurke.
//
//

import UIKit
import ComponentKit

final class AuthenticationPhoneViewController: BaseViewController {
    // MARK: - VIPER Variables
    var presenter: AuthenticationPhonePresenterProtocol {
        get { return self.basePresenter as! AuthenticationPhonePresenterProtocol }
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
    
    private var continueButtonBottomConstraint: NSLayoutConstraint!
    private var continueButtonHeightConstraint: NSLayoutConstraint!
    private var collectionViewBottomConstraint: NSLayoutConstraint!
    
    // MARK: - Members
    
    // MARK: - Lifecycles
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.viewDidLoad()
    }
    
    // MARK: - Custom Methods
}

// MARK: - AuthenticationPhonePresenterDelegate
extension AuthenticationPhoneViewController: AuthenticationPhonePresenterDelegate {
    func didSetTitleAndDescriptionText(_ title: String, _ description: String) {
        titleLabel.text = title
        descriptionLabel.text = description
    }
    
    func updateContinueButtonAppearance(title: String,
                                        isEnabled: Bool,
                                        isHidden: Bool) {
        continueButton.setTitle(title)
        continueButton.setEnabled(isEnabled)
        continueButtonHeightConstraint.isActive = false
        collectionViewBottomConstraint.isActive = false
        continueButton.isHidden = isHidden
        
        continueButtonBottomConstraint = continueButton.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: isHidden ? 0 : -50)
        continueButtonHeightConstraint = continueButton.heightAnchor.constraint(equalToConstant: isHidden ? 0 : 46)
        collectionViewBottomConstraint = collectionView.bottomAnchor.constraint(equalTo: isHidden ? view.bottomAnchor : continueButton.topAnchor)
        
        continueButtonHeightConstraint.isActive = true
        collectionViewBottomConstraint.isActive = true
        continueButtonBottomConstraint.isActive = true

        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.view.layoutIfNeeded()
        }
    }
    
    func continueButtonEnabled(isEnabled: Bool) {
        continueButton.setEnabled(isEnabled)
    }
    
    func prepareUI() {
        textStackView.addArrangedSubviews([titleLabel, descriptionLabel])
        view.addSubview(textStackView)
        view.addSubview(continueButton)
        view.addSubview(collectionView)
        
        continueButtonBottomConstraint = continueButton.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: presenter.isLogin ? 0 : -50)
        continueButtonHeightConstraint = continueButton.heightAnchor.constraint(equalToConstant: presenter.isLogin ? 0 : 46)
        collectionViewBottomConstraint = collectionView.bottomAnchor.constraint(equalTo: presenter.isLogin ? view.bottomAnchor : continueButton.topAnchor)
        
        layoutConstraintActive()
    }
    
    private func layoutConstraintActive() {
        NSLayoutConstraint.activate([
            textStackView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 24),
            textStackView.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 24),
            textStackView.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -24),
            continueButton.leadingAnchor.constraint(equalTo: textStackView.leadingAnchor),
            continueButton.trailingAnchor.constraint(equalTo: textStackView.trailingAnchor),
            continueButtonBottomConstraint,
            continueButtonHeightConstraint,
            collectionView.topAnchor.constraint(equalTo: textStackView.bottomAnchor),
            collectionView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            collectionView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            collectionViewBottomConstraint
        ])
    }
    
    func reloadData() {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.collectionView.reloadData()
        }
    }
}
// MARK: - UICollectionViewDataSource
extension AuthenticationPhoneViewController: UICollectionViewDataSource {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return presenter.numberOfItemsInSection
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        switch indexPath.item {
        case 0:
            let cell = PhoneCollectionViewCell.dequeue(from: collectionView, at: indexPath)
            cell.bind(delegate: self,
                      indexPath: indexPath,
                      title: DesignKitL10n.Authentication.PhoneCell.title,
                      areaText: DesignKitL10n.Authentication.PhoneCell.AreaTextField.text,
                      areaImage: UIImage(named: "turkish-flag"),
                      phonePlaceholder: DesignKitL10n.Authentication.PhoneCell.PhoneTextField.placeholder)
            return cell
        case 1:
            if presenter.isLogin {
                let cell = ButtonCollectionViewCell.dequeue(from: collectionView, at: indexPath)
                cell.bind(delegate: self,
                          title: DesignKitL10n.Authentication.Phone.Login.title,
                          titleColor: DesignKitColorName.contentStrong900.color,
                          backgroundColor: DesignKitColorName.backgroundPrimaryGreen.color,
                          cornerRadius: 23,
                          tag: 1)
                return cell
            } else {
                let cell = HorizontalTitleButtonCollectionViewCell.dequeue(from: collectionView, at: indexPath)
                cell.bind(delegate: self,
                          labelText: DesignKitL10n.Authentication.Register.haveAnAccount,
                          labelTextColor: DesignKitColorName.contentSub800.color,
                          buttonTitle: DesignKitL10n.Authentication.Phone.Login.title,
                          buttonTitleColor: DesignKitColorName.contentStrong900.color,
                          buttonFont: .bold04Compact,
                          buttonTag: 2)
                return cell
            }
            
        case 2:
            let cell = TitleLineCollectionViewCell.dequeue(from: collectionView, at: indexPath)
            cell.bind(text: DesignKitL10n.Authentication.PhoneCell.lineTitle,
                      textColor: DesignKitColorName.contentSoft600.color,
                      lineBackgroundColor: DesignKitColorName.d9d9d9.color,
                      lineHeight: 1)
            return cell
        case 3:
            let cell = ButtonCollectionViewCell.dequeue(from: collectionView, at: indexPath)
            cell.bind(delegate: self,
                      title: DesignKitL10n.Authentication.email,
                      titleColor: .white,
                      backgroundColor: DesignKitColorName.contentStrong900.color,
                      cornerRadius: 23,
                      tag: 3)
            return cell
        case 4:
            let cell = ButtonCollectionViewCell.dequeue(from: collectionView, at: indexPath)
            cell.bind(delegate: self,
                      title: DesignKitL10n.Authentication.google,
                      titleColor: DesignKitColorName.contentStrong900.color,
                      backgroundColor: .clear,
                      cornerRadius: 23,
                      borderWidth: 1,
                      borderColor: DesignKitColorName.borderPrimary.color,
                      image: UIImage(named: "google"),
                      imageTitleSpacing: 4,
                      tag: 4)
            return cell
        case 5:
            let cell = ButtonCollectionViewCell.dequeue(from: collectionView, at: indexPath)
            cell.bind(delegate: self,
                      title: DesignKitL10n.Authentication.facebook,
                      titleColor: DesignKitColorName.contentStrong900.color,
                      backgroundColor: .clear,
                      cornerRadius: 23,
                      borderWidth: 1,
                      borderColor: DesignKitColorName.borderPrimary.color,
                      image: UIImage(named: "facebook"),
                      imageTitleSpacing: 4,
                      tag: 5)
            return cell
            
        case 6:
            let cell = ButtonCollectionViewCell.dequeue(from: collectionView, at: indexPath)
            cell.bind(delegate: self,
                      title: DesignKitL10n.Authentication.apple,
                      titleColor: DesignKitColorName.contentStrong900.color,
                      backgroundColor: .clear,
                      cornerRadius: 23,
                      borderWidth: 1,
                      borderColor: DesignKitColorName.borderPrimary.color,
                      image: UIImage(named: "apple"),
                      imageTitleSpacing: 4,
                      tag: 6)
            return cell
        case 7:
            let cell = HorizontalTitleButtonCollectionViewCell.dequeue(from: collectionView, at: indexPath)
            cell.bind(delegate: self,
                      labelText: DesignKitL10n.Authentication.Login.notAMemberYet,
                      labelTextColor: DesignKitColorName.contentSub800.color,
                      buttonTitle: DesignKitL10n.Onboarding.register,
                      buttonTitleColor: DesignKitColorName.contentStrong900.color,
                      buttonFont: .bold04Compact,
                      buttonTag: 7)
            return cell
        default:
            return UICollectionViewCell.dequeue(from: collectionView, at: indexPath)
        }
    }
}

// MARK: - UICollectionViewDelegateFlowLayout
extension AuthenticationPhoneViewController: UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        switch indexPath.item {
        case 0:
            return CGSize(width: collectionView.frame.width - 48, height: 82)
        case 1:
            if presenter.isLogin {
                return CGSize(width: collectionView.frame.width, height: 46)
            } else {
                return CGSize(width: collectionView.frame.width - 48, height: 16)
            }
        case 2:
            return CGSize(width: collectionView.frame.width - 48, height: 66)
        case 3,4,5,6:
            return CGSize(width: collectionView.frame.width, height: 46)
        case 7:
            return CGSize(width: collectionView.frame.width - 48, height: 54)
        default:
            return .zero
        }
    }
}

// MARK: - CKButtonDelegate
extension AuthenticationPhoneViewController: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        presenter.didTappedButton(tag: tag)
    }
}

// MARK: - PhoneCollectionViewCellDelegate
extension AuthenticationPhoneViewController: PhoneCollectionViewCellDelegate {
    func didChangeText(_ text: String, at indexPath: IndexPath?, tag: Int) {
        presenter.didChangeText(text, tag: tag)
    }
}

// MARK: - ButtonCollectionViewDelegate
extension AuthenticationPhoneViewController: ButtonCollectionViewDelegate {
    func didTappedButton(tag: Int, indexPath: IndexPath?) {
        presenter.didTappedButton(tag: tag)
    }
}

// MARK: - HorizontalTitleButtonCollectionViewCellDelegate
extension AuthenticationPhoneViewController: HorizontalTitleButtonCollectionViewCellDelegate {
    func didTappedButton(tag: Int) {
        presenter.didTappedButton(tag: tag)
    }
}
