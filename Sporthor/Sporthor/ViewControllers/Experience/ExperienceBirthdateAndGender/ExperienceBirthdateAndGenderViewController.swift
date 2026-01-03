//
//  ExperienceBirthdateAndGenderViewController.swift
//  Sporthor
//
//  Created by derTurke on 18.02.2025.
//
//

import UIKit
import ComponentKit

final class ExperienceBirthdateAndGenderViewController: BaseViewController {
    // MARK: - VIPER Variables
    var presenter: ExperienceBirthdateAndGenderPresenterProtocol {
        get { return self.basePresenter as! ExperienceBirthdateAndGenderPresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - UI Elements
    private lazy var progressView: CKProgressView = {
        let progressView = CKProgressView(steps: ApplicationContext.shared.isSelectedCoach ? 5 : 4,
                                          selectedStep: ApplicationContext.shared.isSelectedCoach ? 4 : 3)
        progressView.translatesAutoresizingMaskIntoConstraints = false
        return progressView
    }()
    
    private lazy var titleLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentStrong900.color, numberOfLines: 0, font: .heading04)
        label.translatesAutoresizingMaskIntoConstraints = false
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
            isEnabled: false
        )
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

// MARK: - ExperienceBirthdateAndGenderPresenterDelegate
extension ExperienceBirthdateAndGenderViewController: ExperienceBirthdateAndGenderPresenterDelegate {
    func prepareUI() {
        view.addSubview(progressView)
        view.addSubview(titleLabel)
        view.addSubview(collectionView)
        view.addSubview(continueButton)
        
        NSLayoutConstraint.activate([
            progressView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor),
            progressView.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 24),
            progressView.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -24),
            
            titleLabel.topAnchor.constraint(equalTo: progressView.bottomAnchor, constant: 41),
            titleLabel.leadingAnchor.constraint(equalTo: progressView.leadingAnchor),
            titleLabel.trailingAnchor.constraint(equalTo: progressView.trailingAnchor),
            
            continueButton.leadingAnchor.constraint(equalTo: progressView.leadingAnchor),
            continueButton.trailingAnchor.constraint(equalTo: progressView.trailingAnchor),
            continueButton.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: -50),
            continueButton.heightAnchor.constraint(equalToConstant: 46),
            
            collectionView.topAnchor.constraint(equalTo: titleLabel.bottomAnchor),
            collectionView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            collectionView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            collectionView.bottomAnchor.constraint(equalTo: continueButton.topAnchor)
        ])
    }
    
    func didSetHeaderTitle(_ title: String) {
        titleLabel.text = title
    }
    
    func didSetContinueButtonTitle(_ title: String) {
        continueButton.setTitle(title)
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
        guard let nextCell = collectionView.cellForItem(at: indexPath) as? TextFieldCollectionViewCell else { return }
        nextCell.textFieldBecomeFirstResponder()
    }
}

// MARK: - UICollectionViewDataSource
extension ExperienceBirthdateAndGenderViewController: UICollectionViewDataSource {
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 4
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return section == 1 ? 3 : 1
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        switch indexPath.section {
        case 0:
            let cell = HeaderCollectionViewCell.dequeue(from: collectionView, at: indexPath)
            cell.bind(text: DesignKitL10n.Experience.BirthdateAndGender.birthDateTitle,
                      textColor: DesignKitColorName.contentStrong900.color,
                      font: .heading06)
            return cell
        case 1:
            switch indexPath.row {
            case 0:
                let cell = TextFieldCollectionViewCell.dequeue(from: collectionView, at: indexPath)
                cell.bind(delegate: self,
                          titleText: DesignKitL10n.Experience.BirthdateAndGender.BirthDate.day,
                          textFieldText: presenter.day,
                          textFieldPlaceholder: DesignKitL10n.Experience.BirthdateAndGender.BirthDate.day,
                          textFieldKeyboardType: .numberPad,
                          textFieldMaxLength: BirthdateTextFieldMaxLength.birthdateDay.rawValue(),
                          textFieldTag: 0,
                          indexPath: indexPath)
                return cell
            case 1:
                let cell = TextFieldCollectionViewCell.dequeue(from: collectionView, at: indexPath)
                cell.bind(delegate: self,
                          titleText: DesignKitL10n.Experience.BirthdateAndGender.BirthDate.month,
                          textFieldText: presenter.month,
                          textFieldPlaceholder: DesignKitL10n.Experience.BirthdateAndGender.BirthDate.month,
                          textFieldKeyboardType: .numberPad,
                          textFieldMaxLength: BirthdateTextFieldMaxLength.birthdateMonth.rawValue(),
                          textFieldTag: 1,
                          indexPath: indexPath)
                return cell
            case 2:
                let cell = TextFieldCollectionViewCell.dequeue(from: collectionView, at: indexPath)
                cell.bind(delegate: self,
                          titleText: DesignKitL10n.Experience.BirthdateAndGender.BirthDate.year,
                          textFieldText: presenter.year,
                          textFieldPlaceholder: DesignKitL10n.Experience.BirthdateAndGender.BirthDate.year,
                          textFieldKeyboardType: .numberPad,
                          textFieldMaxLength: BirthdateTextFieldMaxLength.birthDateYear.rawValue(),
                          textFieldTag: 2,
                          indexPath: indexPath)
                return cell
            default:
                break
            }
        case 2:
            let cell = HeaderCollectionViewCell.dequeue(from: collectionView, at: indexPath)
            cell.bind(text: DesignKitL10n.Experience.BirthdateAndGender.gender,
                      textColor: DesignKitColorName.contentStrong900.color,
                      font: .heading06)
            return cell
        case 3:
            let cell = RadioButtonGroupCollectionViewCell.dequeue(from: collectionView, at: indexPath)
            cell.bind(delegate: self,
                      options: presenter.gender,
                      selectedIndex: presenter.selectedGenderIndex)
            return cell
        default:
            break
        }
        return UICollectionViewCell.dequeue(from: collectionView, at: indexPath)
    }
}

// MARK: - UICollectionViewDelegateFlowLayout
extension ExperienceBirthdateAndGenderViewController: UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        switch indexPath.section {
        case 0, 2:
            return CGSize(width: collectionView.frame.width - 48, height: 24)
        case 1:
            return CGSize(width: (collectionView.frame.width - 78) / 3, height: 70)
        case 3:
            return CGSize(width: collectionView.frame.width - 48, height: 94)
        default:
            return .zero
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, insetForSectionAt section: Int) -> UIEdgeInsets {
        switch section {
        case 1:
            return UIEdgeInsets(top: 21, left: 24, bottom: 0, right: 24)
        case 2:
            return UIEdgeInsets(top: 34, left: 0, bottom: 0, right: 0)
        case 3:
            return UIEdgeInsets(top: 16, left: 0, bottom: 0, right: 0)
        default:
            return .zero
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumInteritemSpacingForSectionAt section: Int) -> CGFloat {
        return section == 1 ? 15 : 0
    }
}

// MARK: - TextFieldCollectionViewCellDelegate
extension ExperienceBirthdateAndGenderViewController: TextFieldCollectionViewCellDelegate {
    func textFieldDidChangeSelection(text: String, tag: Int, indexPath: IndexPath?) {
        guard let indexPath = indexPath else { return }
        presenter.checkMaxLengthAndFocusNextTextField(text: text,
                                                      tag: tag,
                                                      indexPath: indexPath)
    }
    
    func textFieldDidEndEditing(text: String, tag: Int, indexPath: IndexPath?) {
        presenter.textFieldDidEndEditing(text: text, tag: tag)
    }
}

extension ExperienceBirthdateAndGenderViewController: CKRadioButtonGroupDelegate {
    func radioButtonGroup(_ group: CKRadioButtonGroup, didSelect index: Int) {
        presenter.selectedGender(index)
    }
}

// MARK: - CKButtonDelegate
extension ExperienceBirthdateAndGenderViewController: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        presenter.didTappedButton(tag)
    }
}
