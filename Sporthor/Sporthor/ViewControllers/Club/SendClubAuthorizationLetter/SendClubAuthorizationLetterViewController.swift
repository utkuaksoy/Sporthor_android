//
//  SendClubAuthorizationLetterViewController.swift
//  Sporthor
//
//  Created by derTurke on 20.05.2025.
//
//

import UIKit
import ComponentKit
import UniformTypeIdentifiers

final class SendClubAuthorizationLetterViewController: BaseViewController {
    // MARK: - VIPER Variables
    var presenter: SendClubAuthorizationLetterPresenterProtocol {
        get { return self.basePresenter as! SendClubAuthorizationLetterPresenterProtocol }
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
                              title: "Gönder",
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
    
    // MARK: - Custom Methods
}

// MARK: - SendClubAuthorizationLetterPresenterDelegate
extension SendClubAuthorizationLetterViewController: SendClubAuthorizationLetterPresenterDelegate {
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
    
    func openDocumentPicker() {
        let doc = UTType(filenameExtension: "doc")!
        let docx = UTType(filenameExtension: "docx")!
        let documentPicker = UIDocumentPickerViewController(forOpeningContentTypes: [.pdf, doc, docx])
        documentPicker.allowsMultipleSelection = false
        documentPicker.delegate = self
        present(documentPicker, animated: true)
    }
}

// MARK: - UICollectionViewDataSource
extension SendClubAuthorizationLetterViewController: UICollectionViewDataSource {
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 4
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        switch section {
        case 1:
            return presenter.files.count
        default:
            return 1
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        switch indexPath.section {
        case 0:
            let cell = ImageTitleAndDescriptionCollectionViewCell.dequeue(from: collectionView, at: indexPath)
            cell.bind(delegate: self,
                      image: presenter.sportClub.logo,
                      title: presenter.sportClub.clubName,
                      description: "")
            return cell
        case 1:
            let file = presenter.files[indexPath.item]
            let cell = UploadDocumentCollectionViewCell.dequeue(from: collectionView, at: indexPath)
            cell.configure(delegate: self,
                           title: file.name ?? "Yetki belgesi yükle",
                           isDelete: !(file.name?.isEmpty ?? true),
                           indexPath: indexPath)
            return cell
        case 2:
            let cell = ButtonCollectionViewCell.dequeue(from: collectionView, at: indexPath)
            cell.bind(delegate: self,
                      title: "Yeni Belge Ekle",
                      titleColor: DesignKitColorName.contentStrong900.color,
                      cornerRadius: 20,
                      borderWidth: 1,
                      borderColor: DesignKitColorName.borderStrong900.color,
                      image: Asset.blackPlus.image,
                      imageTitleSpacing: 4)
            return cell
        case 3:
            let cell = InfoCollectionViewCell.dequeue(from: collectionView, at: indexPath)
            cell.configure(description: "PDF ya da word uzantılarını yükleyebilirsin")
            return cell
        default:
            return UICollectionViewCell.dequeue(from: collectionView, at: indexPath)
        }
    }
    
}

// MARK: - UICollectionViewDelegateFlowLayout
extension SendClubAuthorizationLetterViewController: UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        switch indexPath.section {
        case 0:
            return CGSize(width: collectionView.frame.width - 48, height: 60)
        case 1:
            return CGSize(width: collectionView.frame.width - 48, height: 64)
        case 2:
            return CGSize(width: 200, height: 38)
        case 3:
            return CGSize(width: collectionView.frame.width - 48, height: 40)
        default:
            return .zero
        }
        
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, insetForSectionAt section: Int) -> UIEdgeInsets {
        return UIEdgeInsets(top: 8, left: 0, bottom: 16, right: 0)
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, referenceSizeForHeaderInSection section: Int) -> CGSize {
        guard section < 2 else { return .zero }
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
                reusableView.bind(
                    title: "Seçilen Kulüp",
                    titleFont: .bold03Compact,
                    padding: 24
                )
            case 1:
                reusableView.bind(
                    title: "Kulüpte yetkili olduğunu belgeleyen dokümanları yükle",
                    padding: 24
                )
            default:
                break
            }
            return reusableView
        }
        
        return UICollectionReusableView()
    }
}

// MARK: - ImageTitleAndDescriptionCollectionViewCellDelegate
extension SendClubAuthorizationLetterViewController: ImageTitleAndDescriptionCollectionViewCellDelegate {
    func didTappedDescriptionLabel() {
        
    }
}

// MARK: - UploadDocumentCollectionViewCellDelegate
extension SendClubAuthorizationLetterViewController: UploadDocumentCollectionViewCellDelegate {
    func didTappedUploadDocument(indexPath: IndexPath?, isDelete: Bool) {
        presenter.didTappedUploadDocumentAt(indexPath, isDelete: isDelete)
    }
}

extension SendClubAuthorizationLetterViewController: ButtonCollectionViewDelegate {
    func didTappedButton(tag: Int, indexPath: IndexPath?) {
        presenter.didTappedAddNewDocument()
    }
}

// MARK: - CKButtonDelegate
extension SendClubAuthorizationLetterViewController: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        presenter.didTappedCKButton(tag)
    }
}

// MARK: - UIDocumentPickerDelegate
extension SendClubAuthorizationLetterViewController: UIDocumentPickerDelegate {
    func documentPicker(_ controller: UIDocumentPickerViewController, didPickDocumentsAt urls: [URL]) {
        guard let fileURL = urls.first else { return }
        presenter.copyFile(fileURL)
    }
}
