//
//  CreateTrainingGroupViewController.swift
//  Sporthor
//
//  Created by derTurke on 19.05.2025.
//
//

import UIKit
import ComponentKit
import IQKeyboardManagerSwift

final class CreateTrainingGroupViewController: BaseViewController {
    // MARK: - VIPER Variables
    var presenter: CreateTrainingGroupPresenterProtocol {
        get { return self.basePresenter as! CreateTrainingGroupPresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - UI Elements
    private lazy var textStackView: CKStackView = {
        let stackView = CKStackView(spacing: 8)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.addArrangedSubviews([titleLabel, descriptionLabel])
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
        collectionView.register(HeaderReusableView.self, forSupplementaryViewOfKind: UICollectionView.elementKindSectionHeader, withReuseIdentifier: "HeaderReusableView")
        return collectionView
    }()
    
    private lazy var continueButton: CKButton = {
        let button = CKButton(delegate: self,
                              title: "Devam Et",
                              titleColor: DesignKitColorName.contentStrong900.color,
                              buttonBackgroundColor: DesignKitColorName.backgroundPrimaryGreen.color,
                              cornerRadius: 23,
                              font: .bold03Compact,
                              tag: 0)
        button.heightAnchor.constraint(equalToConstant: 46).isActive = true
        return button
    }()
    
    private lazy var skipButton: CKButton = {
        let button = CKButton(delegate: self,
                              title: "Bu Adımı Atla",
                              titleColor: DesignKitColorName.contentStrong900.color,
                              buttonBackgroundColor: .clear,
                              cornerRadius: 23,
                              borderWidth: 1,
                              borderColor: DesignKitColorName.contentStrong900.color,
                              font: .bold03Compact,
                              tag: 1)
        button.heightAnchor.constraint(equalToConstant: 46).isActive = true
        return button
    }()
    
    private lazy var buttonsStackView: CKStackView = {
        let stackView = CKStackView(spacing: 12)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.addArrangedSubviews([continueButton, skipButton])
        return stackView
    }()
    
    // MARK: - Members
    
    // MARK: - Lifecycles
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.viewDidLoad()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        IQKeyboardManager.shared.resignOnTouchOutside = false
    }

    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        IQKeyboardManager.shared.resignOnTouchOutside = true
    }
    
    // MARK: - Custom Methods
}

// MARK: - CreateTrainingGroupPresenterDelegate
extension CreateTrainingGroupViewController: CreateTrainingGroupPresenterDelegate {
    func didSetTitleAndDescriptionText(_ title: String, _ description: String) {
        titleLabel.text = title
        descriptionLabel.text = description
    }
    
    func prepareUI() {
        view.addSubview(textStackView)
        view.addSubview(buttonsStackView)
        view.addSubview(collectionView)
        
        NSLayoutConstraint.activate([
            textStackView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 24),
            textStackView.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 24),
            textStackView.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -24),
            
            buttonsStackView.leadingAnchor.constraint(equalTo: textStackView.leadingAnchor),
            buttonsStackView.trailingAnchor.constraint(equalTo: textStackView.trailingAnchor),
            buttonsStackView.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: -32),
            
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
    
    func hiddenSkipButton(_ isHidden: Bool) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            skipButton.isHidden = isHidden
        }
    }
}

// MARK: - UICollectionViewDataSource
extension CreateTrainingGroupViewController: UICollectionViewDataSource {
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 3
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return 1
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        switch indexPath.section {
        case 0:
            let cell = ImageTitleAndDescriptionCollectionViewCell.dequeue(from: collectionView, at: indexPath)
            cell.bind(delegate: self,
                      image: presenter.selectedTeam?.image ?? "",
                      title: presenter.selectedTeam?.name ?? "",
                      description: presenter.selectedTeams.count == 1 ? "" : "Değiştir")
            return cell
        case 1:
            let cell = SelectionCollectionViewCell.dequeue(from: collectionView, at: indexPath)
            cell.bind(delegate: self,
                      text: presenter.selectedSeason?.name ?? "")
            return cell
        case 2:
            let cell = SelectionTextFieldCollectionViewCell.dequeue(from: collectionView, at: indexPath)
            cell.bind(delegate: self,
                      items: presenter.suggestionNames,
                      text: presenter.groupName,
                      placeholder: "Grup Adı")
            return cell
        default:
            return UICollectionViewCell.dequeue(from: collectionView, at: indexPath)
        }
    }
    
}

// MARK: - UICollectionViewDelegateFlowLayout
extension CreateTrainingGroupViewController: UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        switch indexPath.section {
        case 0:
            return CGSize(width: collectionView.frame.width - 48, height: 60)
        case 1:
            return CGSize(width: collectionView.frame.width - 48, height: 48)
        case 2:
            return CGSize(width: collectionView.frame.width - 48, height: 148)
        default:
            return .zero
        }
        
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, insetForSectionAt section: Int) -> UIEdgeInsets {
        return UIEdgeInsets(top: 8, left: 0, bottom: 16, right: 0)
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, referenceSizeForHeaderInSection section: Int) -> CGSize {
        return CGSize(width: collectionView.frame.width, height: 24)
    }
    
    func collectionView(_ collectionView: UICollectionView, viewForSupplementaryElementOfKind kind: String, at indexPath: IndexPath) -> UICollectionReusableView {
        if kind == UICollectionView.elementKindSectionHeader {
            let reusableView = collectionView.dequeueReusableSupplementaryView(
                ofKind: kind,
                withReuseIdentifier: "HeaderReusableView",
                for: indexPath
            ) as! HeaderReusableView
            
            switch indexPath.section {
            case 0:
                reusableView.bind(title: "Seçilen Kulüp",
                                  titleFont: .bold03Compact,
                                  padding: 24)
            case 1:
                reusableView.bind(title: "Sezon Seçimi",
                                  padding: 24)
            case 2:
                reusableView.bind(title: "Grup Adı",
                                  padding: 24)
            default:
                break
            }
            return reusableView
        }
        
        return UICollectionReusableView()
    }
}

extension CreateTrainingGroupViewController: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        presenter.didTappedCKButton(tag: tag)
    }
}

extension CreateTrainingGroupViewController: ImageTitleAndDescriptionCollectionViewCellDelegate {
    func didTappedDescriptionLabel() {
        presenter.didTappedChange()
    }
}

// MARK: - SelectionCollectionViewCell
extension CreateTrainingGroupViewController: SelectionCollectionViewCellDelegate {
    func didTappedSelectionCollectionViewCell(tag: Int) {
        presenter.didTappedSelectionCollectionViewCell(tag)
    }
}

extension CreateTrainingGroupViewController: CKSelectionTextFieldDelegate {
    func ckSelectionTextFieldDidEndEditing(_ text: String) {
        presenter.textFieldDidEndEditing(text)
    }
}

