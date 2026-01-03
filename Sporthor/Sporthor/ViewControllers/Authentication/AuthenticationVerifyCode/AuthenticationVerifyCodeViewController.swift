//
//  AuthenticationVerifyCodeViewController.swift
//  Sporthor
//
//  Created by derTurke.
//
//

import UIKit
import ComponentKit

final class AuthenticationVerifyCodeViewController: BaseViewController {
    // MARK: - VIPER Variables
    var presenter: AuthenticationVerifyCodePresenterProtocol {
        get { return self.basePresenter as! AuthenticationVerifyCodePresenterProtocol }
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
    
    // MARK: - Members
    
    // MARK: - Lifecycles
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.viewDidLoad()
    }
    
    // MARK: - Custom Methods
}

// MARK: - AuthenticationVerifyCodePresenterDelegate
extension AuthenticationVerifyCodeViewController: AuthenticationVerifyCodePresenterDelegate {
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
        view.addSubview(continueButton)
        view.addSubview(collectionView)
        layoutConstraintActive()
    }
    
    private func layoutConstraintActive() {
        NSLayoutConstraint.activate([
            textStackView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 24),
            textStackView.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 24),
            textStackView.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -24),
            continueButton.leadingAnchor.constraint(equalTo: textStackView.leadingAnchor),
            continueButton.trailingAnchor.constraint(equalTo: textStackView.trailingAnchor),
            continueButton.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: -50),
            continueButton.heightAnchor.constraint(equalToConstant: 46),
            collectionView.topAnchor.constraint(equalTo: textStackView.bottomAnchor),
            collectionView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            collectionView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            collectionView.bottomAnchor.constraint(equalTo: continueButton.topAnchor)
        ])
    }
    
    func didSetFocusTextField(at indexPath: IndexPath) {
        guard let nextCell = collectionView.cellForItem(at: indexPath) as? TextFieldCollectionViewCell else { return }
        nextCell.textFieldBecomeFirstResponder()
    }
    
    func reloadSection(_ section: Int) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.collectionView.reloadSections(IndexSet(integer: section))
        }
    }
}

// MARK: - UICollectionViewDataSource
extension AuthenticationVerifyCodeViewController: UICollectionViewDataSource {
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 4
    }
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        switch section {
        case 0:
            return 7
        default:
            return 1
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        switch indexPath.section {
        case 0:
            if indexPath.item == 3 {
                let cell = SeperatorCollectionViewCell.dequeue(from: collectionView, at: indexPath)
                cell.bind(color: DesignKitColorName.contentStrong900.color, width: 8, height: 2)
                return cell
            } else {
                let cell = TextFieldCollectionViewCell.dequeue(from: collectionView, at: indexPath)
                cell.bind(delegate: self,
                          textFieldTextAlignment: .center,
                          textFieldFont: .heading05,
                          textFieldKeyboardType: .numberPad,
                          textFieldMaxLength: 1,
                          textFieldTag: indexPath.item > 3 ? indexPath.item - 1 : indexPath.item,
                          indexPath: indexPath)
                return cell
            }
        case 1:
            let cell = CircularCountdownCollectionViewCell.dequeue(from: collectionView, at: indexPath)
            cell.bind(
                delegate: self,
                shapeLayerColor: DesignKitColorName.backgroundPrimaryGreen.color,
                changeShapeLayerColor: DesignKitColorName.red500.color,
                lineWidth: 3,
                duration: 180,
                changeColorDuration: 45
            )
            return cell
        case 2:
            let cell = ButtonCollectionViewCell.dequeue(from: collectionView, at: indexPath)
            cell.bind(delegate: self,
                      title: DesignKitL10n.Authentication.VerifyCode.sendAgainButton,
                      titleColor: DesignKitColorName.contentStrong900.color,
                      disabledTextColor: DesignKitColorName.contentStrong900.color,
                      font: .bold04Compact,
                      isEnabled: presenter.againCodeIsEnabled)
            return cell
        default:
            return UICollectionViewCell.dequeue(from: collectionView, at: indexPath)
        }
    }
}

// MARK: - UICollectionViewDelegateFlowLayout
extension AuthenticationVerifyCodeViewController: UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        switch indexPath.section {
        case 0:
            guard indexPath.item != 3 else {
                return CGSize(width: 8, height: 56)
            }
            return CGSize(width: (collectionView.frame.width - 104) / 6, height: 56)
        case 1:
            return CGSize(width: collectionView.frame.width - 48, height: 48)
        case 2:
            return CGSize(width: collectionView.frame.width - 48, height: 14)
        default:
            return .zero
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, insetForSectionAt section: Int) -> UIEdgeInsets {
        switch section {
        case 0:
            return UIEdgeInsets(top: 0, left: 24, bottom: 0, right: 24)
        case 1:
            return UIEdgeInsets(top: 40, left: 0, bottom: 0, right: 0)
        case 2:
            return UIEdgeInsets(top: 10, left: 0, bottom: 0, right: 0)
        default:
            return .zero
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumInteritemSpacingForSectionAt section: Int) -> CGFloat {
        guard section == 0 else { return .zero }
        return 8
    }
}

// MARK: - TextFieldCollectionViewCellDelegate
extension AuthenticationVerifyCodeViewController: TextFieldCollectionViewCellDelegate {
    func textFieldDidChangeSelection(text: String, tag: Int, indexPath: IndexPath?) {
        guard let indexPath else { return }
        presenter.textFieldDidChangeSelection(text: text,
                                            tag: tag,
                                            indexPath: indexPath)
    }
    
    func textFieldDidEndEditing(text: String, tag: Int, indexPath: IndexPath?) {
        presenter.textFieldDidEndEditing(text: text,
                                       tag: tag,
                                       indexPath: indexPath)
    }
}

// MARK: - UITextFieldDelegate
extension AuthenticationVerifyCodeViewController: UITextFieldDelegate {
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        if string.count == 1 {
            guard let cell = textField.superviewOfType(TextFieldCollectionViewCell.self),
                  let indexPath = collectionView.indexPath(for: cell) else {
                return true
            }

            let adjustedTag = indexPath.item > 3 ? indexPath.item - 1 : indexPath.item
            presenter.updateCode(at: adjustedTag, with: string)
            textField.text = string

            let nextIndex = indexPath.item + 1
            if nextIndex < 7 && nextIndex != 3,
               let nextCell = collectionView.cellForItem(at: IndexPath(item: nextIndex, section: 0)) as? TextFieldCollectionViewCell {
                nextCell.textFieldBecomeFirstResponder()
            } else if nextIndex == 3 {
                if let nextCell = collectionView.cellForItem(at: IndexPath(item: 4, section: 0)) as? TextFieldCollectionViewCell {
                    nextCell.textFieldBecomeFirstResponder()
                }
            }
            
            checkButtonState()
            return false
        }
        
        let otpText = string.filter { $0.isNumber }.prefix(6)
        
        if let firstCell = collectionView.cellForItem(at: IndexPath(item: 0, section: 0)) as? TextFieldCollectionViewCell {
            firstCell.textFieldBecomeFirstResponder()
            
            for i in 0..<7 where i != 3 {
                if let cell = collectionView.cellForItem(at: IndexPath(item: i, section: 0)) as? TextFieldCollectionViewCell {
                    cell.updateText("")
                    let adjustedIndex = i > 3 ? i - 1 : i
                    presenter.updateCode(at: adjustedIndex, with: "")
                }
            }
            
            for (index, char) in otpText.enumerated() {
                let targetIndex = index >= 3 ? index + 1 : index // Separator pozisyonunu atla
                if let cell = collectionView.cellForItem(at: IndexPath(item: targetIndex, section: 0)) as? TextFieldCollectionViewCell {
                    cell.updateText(String(char))
                    presenter.updateCode(at: index, with: String(char))
                    presenter.textFieldDidChangeSelection(text: String(char),
                                                       tag: index,
                                                       indexPath: IndexPath(item: targetIndex, section: 0))
                }
            }
            
            if !otpText.isEmpty {
                let nextIndex = (otpText.count >= 3 ? otpText.count + 1 : otpText.count)
                if nextIndex < 7 && nextIndex != 3,
                   let nextCell = collectionView.cellForItem(at: IndexPath(item: nextIndex, section: 0)) as? TextFieldCollectionViewCell {
                    nextCell.textFieldBecomeFirstResponder()
                } else if nextIndex == 3 {
                    if let nextCell = collectionView.cellForItem(at: IndexPath(item: 4, section: 0)) as? TextFieldCollectionViewCell {
                        nextCell.textFieldBecomeFirstResponder()
                    }
                }
            }
            
            checkButtonState()
        }
        
        return false
    }
    
    private func checkButtonState() {
        presenter.checkButtonState()
    }
}

// MARK: - CKCircularCountdownViewDelegate
extension AuthenticationVerifyCodeViewController: CKCircularCountdownViewDelegate {
    func circularCountdownViewDidFinish() {
        presenter.circularCountdownViewDidFinish()
    }
}

// MARK: - ButtonCollectionViewDelegate
extension AuthenticationVerifyCodeViewController: ButtonCollectionViewDelegate {
    func didTappedButton(tag: Int, indexPath: IndexPath?) {
        presenter.cellButtonClicked(tag: tag, indexPath: indexPath)
    }
}

// MARK: - CKButtonDelegate
extension AuthenticationVerifyCodeViewController: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        presenter.didTappedButton(tag: tag)
    }
}
extension UIView {
    func superviewOfType<T: UIView>(_ type: T.Type) -> T? {
        var view = self.superview
        while let current = view {
            if let match = current as? T {
                return match
            }
            view = current.superview
        }
        return nil
    }
}
