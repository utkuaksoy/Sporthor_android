//
//  GroupNameView.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 27.03.2025.
//
//

import UIKit
import ComponentKit
import DesignKit
import PhotosUI
import AVFoundation

protocol GroupNameViewDelegate: AnyObject {
    func presentImagePickerOptions()
    func presentCamera()
    func presentPhotoLibrary()
    func showPermissionAlert(for type: PermissionType)
}

enum PermissionType: String {
    case camera = "Kamera"
    case photoLibrary = "Galeri"
    case location = "Konum"
}

final class GroupNameView: UIView {
    
    // MARK: - Private UI Elements
    
    weak var delegate: GroupNameViewDelegate?
    
    private lazy var containerStackView: CKStackView = {
        let stackView = CKStackView(
            axis: .horizontal,
            distribution: .fill,
            alignment: .center,
            spacing: .zero
        )
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var imageContainerView: UIView = {
        let view = UIView()
        view.backgroundColor = ColorName.backgroundSoft200.color
        view.layer.cornerRadius = 24
        view.clipsToBounds = true
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var imageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFit
        imageView.image = Asset.groupImagePlusIcon.image
        imageView.tintColor = ColorName.contentSoft600.color
        imageView.layer.cornerRadius = 24
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.isUserInteractionEnabled = true
        return imageView
    }()
    
    private lazy var textField: CKTextField = {
        let textField = CKTextField(
            textColor: DesignKitColorName.contentStrong900.color,
            placeholder: "Grup adını giriniz",
            placeholderColor: DesignKitColorName.contentSoft600.color,
            backgroundColor: ColorName.backgroundWhite0.color,
            cornerRadius: .zero,
            selectedBorderColor: .clear,
            font: .body04Compact
        )
        textField.translatesAutoresizingMaskIntoConstraints = false
        return textField
    }()
    
    // MARK: - Initialize
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupViews()
        setupConstraints()
        setupGestureRecognizer()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    // MARK: - Setup
    
    private func setupViews() {
        addSubview(containerStackView)
        imageContainerView.addSubview(imageView)
        containerStackView.addArrangedSubviews([imageContainerView, textField])
    }
    
    private func setupConstraints() {
        NSLayoutConstraint.activate([
            containerStackView.leadingAnchor.constraint(equalTo: leadingAnchor),
            containerStackView.trailingAnchor.constraint(equalTo: trailingAnchor),
            containerStackView.topAnchor.constraint(equalTo: topAnchor, constant: 12),
            containerStackView.bottomAnchor.constraint(equalTo: bottomAnchor),
            
            imageContainerView.widthAnchor.constraint(equalToConstant: 48),
            imageContainerView.heightAnchor.constraint(equalToConstant: 48),
            
            imageView.leadingAnchor.constraint(equalTo: imageContainerView.leadingAnchor),
            imageView.trailingAnchor.constraint(equalTo: imageContainerView.trailingAnchor),
            imageView.topAnchor.constraint(equalTo: imageContainerView.topAnchor),
            imageView.bottomAnchor.constraint(equalTo: imageContainerView.bottomAnchor),
            
            textField.heightAnchor.constraint(equalToConstant: 48)
        ])
    }
    
    private func setupGestureRecognizer() {
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(imageViewTapped))
        imageView.addGestureRecognizer(tapGesture)
    }
    
    @objc private func imageViewTapped() {
        delegate?.presentImagePickerOptions()
    }
    
    private func checkCameraPermission() {
        switch AVCaptureDevice.authorizationStatus(for: .video) {
        case .authorized:
            delegate?.presentCamera()
        case .notDetermined:
            AVCaptureDevice.requestAccess(for: .video) { [weak self] granted in
                if granted {
                    DispatchQueue.main.async {
                        self?.delegate?.presentCamera()
                    }
                }
            }
        default:
            delegate?.showPermissionAlert(for: .camera)
        }
    }
    
    private func checkPhotoLibraryPermission() {
        switch PHPhotoLibrary.authorizationStatus(for: .readWrite) {
        case .authorized:
            delegate?.presentPhotoLibrary()
        case .notDetermined:
            PHPhotoLibrary.requestAuthorization(for: .readWrite) { [weak self] status in
                if status == .authorized {
                    DispatchQueue.main.async {
                        self?.delegate?.presentPhotoLibrary()
                    }
                }
            }
        default:
            delegate?.showPermissionAlert(for: .photoLibrary)
        }
    }
    
    // MARK: - Public Methods
    
    func setText(_ text: String) {
        textField.text = text
    }
    
    func getText() -> String? {
        return textField.text
    }
    
    func handleCameraSelection() {
        checkCameraPermission()
    }
    
    func handlePhotoLibrarySelection() {
        checkPhotoLibraryPermission()
    }
    
    func configureImageView(image: UIImage) {
        imageView.image = image
    }
}
