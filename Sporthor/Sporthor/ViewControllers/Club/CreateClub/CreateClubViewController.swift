//
//  CreateClubViewController.swift
//  Sporthor
//
//  Created by derTurke on 19.05.2025.
//
//

import UIKit
import ComponentKit
import PhotosUI

final class CreateClubViewController: BaseViewController {
    // MARK: - VIPER Variables
    var presenter: CreateClubPresenterProtocol {
        get { return self.basePresenter as! CreateClubPresenterProtocol }
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
    
    private lazy var clubLogoImageBorderView: UIView = {
        let view = UIView()
        view.setCornerRadius(36)
        view.clipsToBounds = true
        view.backgroundColor = DesignKitColorName.successLighter100.color
        view.setBorderWidth(1)
        view.setBorderColor(DesignKitColorName.backgroundPrimaryGreen.color)
        view.translatesAutoresizingMaskIntoConstraints = false
        view.widthAnchor.constraint(equalToConstant: 72).isActive = true
        view.heightAnchor.constraint(equalToConstant: 72).isActive = true
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(didTappedAddLogo))
        view.isUserInteractionEnabled = true
        view.addGestureRecognizer(tapGesture)
        return view
    }()
    
    private lazy var clubLogoImageView: UIImageView = {
        let imageView = UIImageView(image: Asset.imagePlus.image)
        imageView.setCornerRadius(31)
        imageView.clipsToBounds = true
        imageView.contentMode = .center
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.widthAnchor.constraint(equalToConstant: 62).isActive = true
        imageView.heightAnchor.constraint(equalToConstant: 62).isActive = true
        return imageView
    }()
    
    private lazy var changeClubLogoBackgroundImageView: UIImageView = {
        let imageView = UIImageView(image: Asset.ellipsePrimaryBackground.image)
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.widthAnchor.constraint(equalToConstant: 28).isActive = true
        imageView.heightAnchor.constraint(equalToConstant: 28).isActive = true
        imageView.isHidden = true
        imageView.isUserInteractionEnabled = true
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(didTappedAddLogo))
        imageView.addGestureRecognizer(tapGesture)
        return imageView
    }()
    
    private lazy var changeClubLogoImageView: UIImageView = {
        let imageView = UIImageView(image: Asset.change.image)
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.widthAnchor.constraint(equalToConstant: 18).isActive = true
        imageView.heightAnchor.constraint(equalToConstant: 18).isActive = true
        return imageView
    }()
    
    private lazy var addClubLabel: CKLabel = {
        let label = CKLabel(text: "Kulüp Logosu Ekle",
                            textColor: DesignKitColorName.contentStrong900.color,
                            font: .bold03Compact,
                            isUserInteractionEnabled: true)
        label.translatesAutoresizingMaskIntoConstraints = false
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(didTappedAddLogo))
        label.isUserInteractionEnabled = true
        label.addGestureRecognizer(tapGesture)
        return label
    }()
    
    private lazy var collectionView: UICollectionView = {
        let collectionView = UICollectionView(frame: .zero,
                                              collectionViewLayout: UICollectionViewFlowLayout())
        collectionView.delegate = self
        collectionView.dataSource = self
        collectionView.translatesAutoresizingMaskIntoConstraints = false
        collectionView.contentInset = UIEdgeInsets(top: 16, left: 0, bottom: 16, right: 0)
        collectionView.backgroundColor = .clear
        collectionView.register(HeaderReusableView.self, forSupplementaryViewOfKind: UICollectionView.elementKindSectionHeader, withReuseIdentifier: "HeaderReusableView")
        return collectionView
    }()
    
    private lazy var continueButton: CKButton = {
        let button = CKButton(
            delegate: self,
            title: "Kulübü Oluştur",
            titleColor: DesignKitColorName.contentStrong900.color,
            buttonBackgroundColor: DesignKitColorName.backgroundPrimaryGreen.color,
            cornerRadius: 23,
            font: .bold03Compact)
        button.translatesAutoresizingMaskIntoConstraints = false
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
    
    // MARK: - Custom Methods
    @objc private func didTappedAddLogo() {
        presenter.presentImagePickerOptions()
    }
    
    private func setSelectedImage() {
        clubLogoImageView.contentMode = .scaleAspectFill
        clubLogoImageView.isUserInteractionEnabled = false
        clubLogoImageBorderView.setBorderColor(DesignKitColorName.borderSoft200.color)
        clubLogoImageBorderView.backgroundColor = .clear
        changeClubLogoBackgroundImageView.isHidden = false
        addClubLabel.text = "Kulüp Logosunu Değiştir"
    }
}

// MARK: - CreateClubPresenterDelegate
extension CreateClubViewController: CreateClubPresenterDelegate {
    func didSetTitleAndDescriptionText(_ title: String, _ description: String) {
        titleLabel.text = title
        descriptionLabel.text = description
    }
    
    func prepareUI() {
        view.addSubview(textStackView)
        clubLogoImageBorderView.addSubview(clubLogoImageView)
        view.addSubview(clubLogoImageBorderView)
        changeClubLogoBackgroundImageView.addSubview(changeClubLogoImageView)
        view.addSubview(changeClubLogoBackgroundImageView)
        view.addSubview(addClubLabel)
        view.addSubview(collectionView)
        view.addSubview(continueButton)
        
        NSLayoutConstraint.activate([
            textStackView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 24),
            textStackView.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 24),
            textStackView.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -24),
            
            clubLogoImageBorderView.topAnchor.constraint(equalTo: textStackView.bottomAnchor, constant: 24),
            clubLogoImageBorderView.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 24),
            
            clubLogoImageView.centerXAnchor.constraint(equalTo: clubLogoImageBorderView.centerXAnchor),
            clubLogoImageView.centerYAnchor.constraint(equalTo: clubLogoImageBorderView.centerYAnchor),
            
            changeClubLogoBackgroundImageView.bottomAnchor.constraint(equalTo: clubLogoImageBorderView.bottomAnchor, constant: 2),
            changeClubLogoBackgroundImageView.trailingAnchor.constraint(equalTo: clubLogoImageBorderView.trailingAnchor, constant: 2),
            
            changeClubLogoImageView.centerXAnchor.constraint(equalTo: changeClubLogoBackgroundImageView.centerXAnchor),
            changeClubLogoImageView.centerYAnchor.constraint(equalTo: changeClubLogoBackgroundImageView.centerYAnchor),
            
            addClubLabel.centerYAnchor.constraint(equalTo: clubLogoImageBorderView.centerYAnchor),
            addClubLabel.leadingAnchor.constraint(equalTo: clubLogoImageBorderView.trailingAnchor, constant: 16),
            addClubLabel.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -24),
            
            collectionView.topAnchor.constraint(equalTo: clubLogoImageBorderView.bottomAnchor),
            collectionView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            collectionView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            
            continueButton.topAnchor.constraint(equalTo: collectionView.bottomAnchor),
            continueButton.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 24),
            continueButton.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -24),
            continueButton.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: -32)
        ])
    }
    
    func presentCamera(sourceType: UIImagePickerController.SourceType) {
        let picker = UIImagePickerController()
        picker.delegate = self
        picker.sourceType = sourceType
        picker.mediaTypes = ["public.image"]
        picker.allowsEditing = true
        picker.modalPresentationStyle = .fullScreen
        presenter.presentCamera(picker)
    }
    
    func reloadData() {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.collectionView.reloadData()
        }
    }
    
    func setEditClubImage(_ url: String) {
        clubLogoImageView.setImage(with: url)
        setSelectedImage()
    }
    
    func setContinueButtonTitle(_ title: String) {
        continueButton.setTitle(title)
    }
    
    func setNavigationBarHidden(_ isHidden: Bool) {
        navigationController?.setNavigationBarHidden(isHidden, animated: true)
    }
}

// MARK: - UIImagePickerControllerDelegate, UINavigationControllerDelegate
extension CreateClubViewController: UIImagePickerControllerDelegate, UINavigationControllerDelegate {
    func imagePickerController(
        _ picker: UIImagePickerController,
        didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]
    ) {
        picker.dismiss(animated: true)
        if let image = info[.editedImage] as? UIImage {
            clubLogoImageView.image = image
            setSelectedImage()
            presenter.uploadImage(image)
        }
    }
    
    func imagePickerControllerDidCancel(
        _ picker: UIImagePickerController
    ) {
        picker.dismiss(animated: true)
    }
}

// MARK: - UICollectionViewDataSource
extension CreateClubViewController: UICollectionViewDataSource {
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 4
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        switch section {
        case 1:
            return 2
        default:
            return 1
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        switch indexPath.section {
        case 0:
            let cell = TextFieldCollectionViewCell.dequeue(from: collectionView, at: indexPath)
            cell.bind(delegate: self,
                      titleText: "Kulüp Adı",
                      textFieldText: presenter.clubName,
                      textFieldPlaceholder: "Kulüp Adı",
                      textFieldMaxLength: 255,
                      textFieldTag: 0,
                      statusImageWidth: 0,
                      statusImageHeight: 0)
            return cell
        case 1:
            switch indexPath.item {
            case 0:
                let cell = SelectionCollectionViewCell.dequeue(from: collectionView, at: indexPath)
                cell.bind(delegate: self,
                          text: presenter.location,
                          placeholder: "Adres Seçiniz",
                          tag: 1)
                return cell
            case 1:
                let cell = TextFieldCollectionViewCell.dequeue(from: collectionView, at: indexPath)
                cell.bind(delegate: self,
                          textFieldText: presenter.fullAddress,
                          textFieldPlaceholder: "Açık Adres",
                          textFieldMaxLength: 255,
                          textFieldTag: 1,
                          statusImageWidth: 0,
                          statusImageHeight: 0)
                return cell
            default:
                return UICollectionViewCell.dequeue(from: collectionView, at: indexPath)
            }
        case 2:
            let cell = SelectionCollectionViewCell.dequeue(from: collectionView, at: indexPath)
            cell.bind(delegate: self,
                      text: presenter.selectedBranch?.name ?? "",
                      placeholder: "Branş Seçiniz",
                      tag: 2)
            return cell
        case 3:
            let cell = DatePickerCollectionViewCell.dequeue(from: collectionView, at: indexPath)
            cell.bind(delegate: self,
                      type: .year,
                      titleText: "Kuruluş Tarihi (Opsiyonel)",
                      textFieldText: presenter.foundationDate ?? "",
                      textFieldPlaceholder: "Kuruluş Tarihi (Opsiyonel)",
                      textFieldMaxLength: 4,
                      textFieldTag: 2,
                      statusImageWidth: 0,
                      statusImageHeight: 0)
            return cell
        default:
            return UICollectionViewCell.dequeue(from: collectionView, at: indexPath)
        }
    }
}

// MARK: - UICollectionViewDelegateFlowLayout
extension CreateClubViewController: UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        switch indexPath.section {
        case 0:
            return CGSize(width: collectionView.frame.width - 48, height: 72)
        case 1:
            switch indexPath.item {
            case 0:
                return CGSize(width: collectionView.frame.width - 48, height: 48)
            case 1:
                return CGSize(width: collectionView.frame.width - 48, height: 58)
            default:
                return .zero
            }
        case 2:
            return CGSize(width: collectionView.frame.width - 48, height: 48)
        case 3:
            return CGSize(width: collectionView.frame.width - 48, height: 72)
        default:
            return .zero
        }
        
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, insetForSectionAt section: Int) -> UIEdgeInsets {
        switch section {
        case 1:
            return UIEdgeInsets(top: 8, left: 24, bottom: 16, right: 24)
        default:
            return UIEdgeInsets(top: 8, left: 0, bottom: 16, right: 0)
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, referenceSizeForHeaderInSection section: Int) -> CGSize {
        if section == 1 || section == 2 {
            return CGSize(width: collectionView.frame.width, height: 24)
        } else {
            return .zero
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, viewForSupplementaryElementOfKind kind: String, at indexPath: IndexPath) -> UICollectionReusableView {
        if kind == UICollectionView.elementKindSectionHeader {
            let reusableView = collectionView.dequeueReusableSupplementaryView(
                ofKind: kind,
                withReuseIdentifier: "HeaderReusableView",
                for: indexPath
            ) as! HeaderReusableView
            
            switch indexPath.section {
            case 1:
                reusableView.bind(title: "Kulüp Adresi", padding: 24)
            case 2:
                reusableView.bind(title: "Branş", padding: 24)
            default:
                break
            }
            return reusableView
        }
        
        return UICollectionReusableView()
    }
}

// MARK: - TextFieldCollectionViewCellDelegate
extension CreateClubViewController: TextFieldCollectionViewCellDelegate {
    func textFieldDidEndEditing(text: String, tag: Int, indexPath: IndexPath?) {
        presenter.textFieldDidEndEditing(text, tag: tag)
    }
}

extension CreateClubViewController: SelectionCollectionViewCellDelegate {
    func didTappedSelectionCollectionViewCell(tag: Int) {
        presenter.didTapSelection(tag)
    }
}

extension CreateClubViewController: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        presenter.didTappedContinueButton()
    }
}

extension CreateClubViewController: DatePickerCollectionViewCellDelegate {
    func selectedDatePicker(text: String, tag: Int, indexPath: IndexPath?) {
        presenter.textFieldDidEndEditing(text, tag: tag)
    }
}
