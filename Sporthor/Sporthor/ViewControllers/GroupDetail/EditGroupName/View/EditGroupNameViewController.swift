//
//  EditGroupNameViewController.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 12.04.2025.
//
//

import UIKit
import ComponentKit
import DesignKit
import PhotosUI

final class EditGroupNameViewController: BaseViewController {
    
    // MARK: - VIPER Variables
    var presenter: EditGroupNamePresenterProtocol {
        get { return self.basePresenter as! EditGroupNamePresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - Private UI Elements
    
    private var backButton: UIBarButtonItem {
        let backImage = Asset.chevronLeftIcon.image.withRenderingMode(.alwaysOriginal)
        return UIBarButtonItem(
            image: backImage,
            style: .plain,
            target: self,
            action: #selector(didTappedBackButton)
        )
    }
    
    private lazy var groupImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFill
        imageView.clipsToBounds = true
        imageView.layer.cornerRadius = 40
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()
    
    private lazy var groupNameTextField: CKTextField = {
        let textField = CKTextField()
        textField.placeholder = "Grup Adı"
        textField.font = .bold04Compact
        textField.textColor = .black
        textField.translatesAutoresizingMaskIntoConstraints = false
        return textField
    }()
    
    private lazy var sportIconsCollectionView: UICollectionView = {
        let layout = UICollectionViewFlowLayout()
        layout.scrollDirection = .vertical
        layout.minimumInteritemSpacing = 16
        layout.minimumLineSpacing = 16
        layout.sectionInset = UIEdgeInsets(top: 16, left: 16, bottom: 16, right: 16)
        
        let collectionView = UICollectionView(frame: .zero, collectionViewLayout: layout)
        collectionView.backgroundColor = .clear
        collectionView.delegate = self
        collectionView.dataSource = self
        collectionView.showsVerticalScrollIndicator = false
        collectionView.translatesAutoresizingMaskIntoConstraints = false
        return collectionView
    }()
    
    private lazy var saveButton: CKButton = {
        let button = CKButton(
            title: "Kaydet",
            titleColor: .black,
            buttonBackgroundColor: ColorName.backgroundPrimaryGreen.color,
            cornerRadius: 10
        )
        button.addTarget(self, action: #selector(saveButtonTapped), for: .touchUpInside)
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()
    
    // MARK: - Members
    private var selectedSportIcon: String?
    private var sportIcons: [SportIconModel] = []
    
    // MARK: - Lifecycles

    override func viewDidLoad() {
        super.viewDidLoad()
        setupViews()
        setupConstraints()
        configureNavigationBar()
        presenter.viewDidLoad()
    }
        
    private func configureNavigationBar() {
        let appearance = UINavigationBarAppearance()
        appearance.configureWithOpaqueBackground()
        appearance.backgroundColor = .white

        appearance.titleTextAttributes = [
            .foregroundColor: ColorName.contentStrong900.color,
            .font: UIFont.bold03Compact
        ]

        navigationController?.navigationBar.standardAppearance = appearance
        navigationController?.navigationBar.scrollEdgeAppearance = appearance
        navigationController?.navigationBar.compactAppearance = appearance
        navigationItem.title =  "Düzenle"
        navigationItem.leftBarButtonItem = backButton
    }

    // MARK: - Actions

    @objc
    private func saveButtonTapped() {
        presenter.saveButtonTapped(name: groupNameTextField.text)
    }
    
    @objc
    private func didTappedBackButton() {
        navigationController?.popViewController(animated: true)
    }
}

// MARK: - EditGroupNamePresenterDelegate
extension EditGroupNameViewController: EditGroupNamePresenterDelegate {
    func updateUI(with response: EditGroupNameResponse) {
        groupNameTextField.text = response.groupName
        groupImageView.setImage(with: response.groupImageUrl)
        selectedSportIcon = response.groupImageUrl
        sportIcons = response.icons
        sportIconsCollectionView.reloadData()
    }
}

// MARK: - UICollectionViewDataSource
extension EditGroupNameViewController: UICollectionViewDataSource {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return sportIcons.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        guard let cell = collectionView.dequeueReusableCell(withReuseIdentifier: SportIconCell.reuseIdentifier, for: indexPath) as? SportIconCell,
              let item = sportIcons[safe: indexPath.item]
        else {
            return collectionView.dequeueEmptyReusableCell(with: indexPath)
        }
        
        let isSelected = item.iconPath == selectedSportIcon
        cell.configure(with: item, isSelected: isSelected)
        return cell
    }
}

// MARK: - UICollectionViewDelegateFlowLayout
extension EditGroupNameViewController: UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let width = (collectionView.bounds.width - 80) / 4
        return CGSize(width: width, height: width)
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        if indexPath.row == .zero {
            presentImagePickerOptions()
        } else {
            if let item = sportIcons[safe: indexPath.item] {
                selectedSportIcon = item.iconPath
                groupImageView.setImage(with: item.iconPath)
                presenter.updateProfileImage(imagePath: item.iconPath)
                collectionView.reloadData()
            }
        }
    }
}

private extension EditGroupNameViewController {
    func setupViews() {
        view.addSubview(groupImageView)
        view.addSubview(groupNameTextField)
        view.addSubview(sportIconsCollectionView)
        view.addSubview(saveButton)
        
        sportIconsCollectionView.register(SportIconCell.self, forCellWithReuseIdentifier: SportIconCell.identifier)
    }
    
    func setupConstraints() {
        NSLayoutConstraint.activate([
            groupImageView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 16),
            groupImageView.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            groupImageView.widthAnchor.constraint(equalToConstant: 80),
            groupImageView.heightAnchor.constraint(equalToConstant: 80),
            
            groupNameTextField.topAnchor.constraint(equalTo: groupImageView.bottomAnchor, constant: 16),
            groupNameTextField.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            groupNameTextField.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),
            groupNameTextField.heightAnchor.constraint(equalToConstant: 44),
            
            sportIconsCollectionView.topAnchor.constraint(equalTo: groupNameTextField.bottomAnchor, constant: 16),
            sportIconsCollectionView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            sportIconsCollectionView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            sportIconsCollectionView.bottomAnchor.constraint(equalTo: saveButton.topAnchor, constant: -16),
            
            saveButton.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            saveButton.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),
            saveButton.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: -16),
            saveButton.heightAnchor.constraint(equalToConstant: 50)
        ])
    }
}

private extension EditGroupNameViewController {
    func presentImagePickerOptions() {
        let alertController = UIAlertController(title: nil, message: nil, preferredStyle: .actionSheet)
        
        let cameraAction = UIAlertAction(title: "Kamera", style: .default) { [weak self] _ in
            self?.checkCameraPermission()
        }
        
        let galleryAction = UIAlertAction(title: "Galeri", style: .default) { [weak self] _ in
            self?.checkPhotoLibraryPermission()
        }
        
        let cancelAction = UIAlertAction(title: "İptal", style: .cancel)
        
        alertController.addAction(cameraAction)
        alertController.addAction(galleryAction)
        alertController.addAction(cancelAction)
        
        present(alertController, animated: true)
    }
    
    func checkCameraPermission() {
        switch AVCaptureDevice.authorizationStatus(for: .video) {
        case .authorized:
            self.presentCamera()
        case .notDetermined:
            AVCaptureDevice.requestAccess(for: .video) { [weak self] granted in
                if granted {
                    DispatchQueue.main.async {
                        self?.presentCamera()
                    }
                }
            }
        default:
            showPermissionAlert(for: .camera)
        }
    }
    
    func checkPhotoLibraryPermission() {
        switch PHPhotoLibrary.authorizationStatus(for: .readWrite) {
        case .authorized:
            self.presentPhotoLibrary()
        case .notDetermined:
            PHPhotoLibrary.requestAuthorization(for: .readWrite) { [weak self] status in
                if status == .authorized {
                    DispatchQueue.main.async {
                        self?.presentPhotoLibrary()
                    }
                }
            }
        default:
           showPermissionAlert(for: .photoLibrary)
        }
    }
    
    func presentCamera() {
        let imagePicker = UIImagePickerController()
        imagePicker.sourceType = .camera
        imagePicker.delegate = self
        present(imagePicker, animated: true)
    }
    
    func presentPhotoLibrary() {
        var config = PHPickerConfiguration()
        config.filter = .images
        config.selectionLimit = 1
        
        let picker = PHPickerViewController(configuration: config)
        picker.delegate = self
        present(picker, animated: true)
    }
    
    func showPermissionAlert(for type: PermissionType) {
        let alertController = UIAlertController(
            title: "İzin Gerekli",
            message: "\(type.rawValue) erişimi için ayarlardan izin vermeniz gerekmektedir.",
            preferredStyle: .alert
        )
        
        let settingsAction = UIAlertAction(title: "Ayarlar", style: .default) { _ in
            if let settingsURL = URL(string: UIApplication.openSettingsURLString) {
                UIApplication.shared.open(settingsURL)
            }
        }
        
        let cancelAction = UIAlertAction(title: "İptal", style: .cancel)
        
        alertController.addAction(settingsAction)
        alertController.addAction(cancelAction)
        
        present(alertController, animated: true)
    }
}

extension EditGroupNameViewController: UIImagePickerControllerDelegate, UINavigationControllerDelegate {
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        picker.dismiss(animated: true)
        if let image = info[.originalImage] as? UIImage {
            groupImageView.image = image
            uploadImage(image)
        }
    }
    
    func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
        picker.dismiss(animated: true)
    }
    
    private func uploadImage(_ image: UIImage) {
        presenter.uploadImage(with: image)
    }
}

extension EditGroupNameViewController: PHPickerViewControllerDelegate {
    func picker(_ picker: PHPickerViewController, didFinishPicking results: [PHPickerResult]) {
        picker.dismiss(animated: true)
        
        guard let result = results.first else { return }
        
        result.itemProvider.loadObject(ofClass: UIImage.self) { [weak self] object, error in
            if let image = object as? UIImage {
                DispatchQueue.main.async {
                    guard let self = self else { return }
                    self.groupImageView.image = image
                    self.uploadImage(image)
                }
            }
        }
    }
}
