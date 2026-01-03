//
//  UploadDocumentCollectionViewCell.swift
//  Sporthor
//
//  Created by derTurke on 20.05.2025.
//

import UIKit
import ComponentKit

protocol UploadDocumentCollectionViewCellDelegate: AnyObject {
    func didTappedUploadDocument(indexPath: IndexPath?,
                                 isDelete: Bool)
}

extension UploadDocumentCollectionViewCellDelegate {
    func didTappedUploadDocument(indexPath: IndexPath?,
                                 isDelete: Bool) {}
}

final class UploadDocumentCollectionViewCell: UICollectionViewCell {
    // MARK: - UI Elements
    private lazy var iconBackgroundView: UIView = {
        let view = UIView()
        view.backgroundColor = DesignKitColorName.primaryPink.color
        view.setCornerRadius(8)
        view.translatesAutoresizingMaskIntoConstraints = false
        view.heightAnchor.constraint(equalToConstant: 48).isActive = true
        view.widthAnchor.constraint(equalToConstant: 48).isActive = true
        return view
    }()
    
    private lazy var iconImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.widthAnchor.constraint(equalToConstant: 24).isActive = true
        imageView.heightAnchor.constraint(equalToConstant: 24).isActive = true
        return imageView
    }()
    
    private lazy var documentNameLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentStrong900.color,
                            numberOfLines: 0,
                            font: .bold04Compact)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var rightButton: CKButton = {
        let button = CKButton(delegate: self,
                              image: Asset.addPlusIcon.image)
        button.setCornerRadius(15)
        button.clipsToBounds = true
        button.translatesAutoresizingMaskIntoConstraints = false
        button.heightAnchor.constraint(equalToConstant: 30).isActive = true
        button.widthAnchor.constraint(equalToConstant: 30).isActive = true
        return button
    }()
    
    // MARK: - Members
    private weak var delegate: UploadDocumentCollectionViewCellDelegate?
    private var indexPath: IndexPath?
    private var isDelete: Bool = false
    
    // MARK: - Initializers
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupView()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        setupView()
    }
    
    private func setupView() {
        contentView.setCornerRadius(8)
        iconBackgroundView.addSubview(iconImageView)
        contentView.addSubview(iconBackgroundView)
        contentView.addSubview(documentNameLabel)
        contentView.addSubview(rightButton)
        
        NSLayoutConstraint.activate([
            iconBackgroundView.topAnchor.constraint(equalTo: contentView.topAnchor, constant: 8),
            iconBackgroundView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 8),
            iconBackgroundView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor, constant: -8),
            
            iconImageView.centerXAnchor.constraint(equalTo: iconBackgroundView.centerXAnchor),
            iconImageView.centerYAnchor.constraint(equalTo: iconBackgroundView.centerYAnchor),
            
            documentNameLabel.leadingAnchor.constraint(equalTo: iconBackgroundView.trailingAnchor, constant: 12),
            documentNameLabel.centerYAnchor.constraint(equalTo: iconBackgroundView.centerYAnchor),
            
            rightButton.leadingAnchor.constraint(equalTo: documentNameLabel.trailingAnchor, constant: 12),
            rightButton.centerYAnchor.constraint(equalTo: iconBackgroundView.centerYAnchor),
            rightButton.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -12)
        ])
    }
    
    // MARK: - Custom Methods
    func configure(
        delegate: UploadDocumentCollectionViewCellDelegate? = nil,
        title: String = "Yetki belgesi yükle",
        icon: UIImage = Asset.fileUploadWhite.image,
        iconBackgroundColor: UIColor = DesignKitColorName.primaryPink.color,
        rightButtonImage: UIImage = Asset.addPlusIcon.image,
        backgroundColor: UIColor = DesignKitColorName.backgroundWeak100.color,
        isDelete: Bool = false,
        isDeleteIcon: UIImage = Asset.closeBackgroundBlack.image,
        indexPath: IndexPath? = nil
    ) {
        self.delegate = delegate
        documentNameLabel.text = title
        iconImageView.image = icon
        iconImageView.backgroundColor = iconBackgroundColor
        rightButton.setImage(isDelete ? isDeleteIcon : rightButtonImage)
        contentView.backgroundColor = backgroundColor
        self.isDelete = isDelete
        self.indexPath = indexPath
    }
}

// MARK: - CKButtonDelegate
extension UploadDocumentCollectionViewCell: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        delegate?.didTappedUploadDocument(indexPath: indexPath,
                                          isDelete: isDelete)
    }
}
