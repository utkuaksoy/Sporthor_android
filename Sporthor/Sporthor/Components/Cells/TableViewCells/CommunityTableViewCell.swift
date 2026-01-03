//
//  CommunityTableViewCell.swift
//  Sporthor
//
//  Created by derTurke on 27.05.2025.
//

import UIKit
import ComponentKit

protocol CommunityTableViewCellDelegate: AnyObject {
    func didTappedCommunityCell()
}

extension CommunityTableViewCellDelegate {
    func didTappedCommunityCell() {}
}

final class CommunityTableViewCell: UITableViewCell {
    private lazy var leftIconImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.contentMode = .scaleAspectFill
        return imageView
    }()
    
    private lazy var titleLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentStrong900.color,
                            numberOfLines: 0,
                            font: .bold03Compact)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var profileImagesStackView: CKStackView = {
        let stackView = CKStackView(axis: .horizontal, alignment: .center, spacing: -8)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var rightIconImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.contentMode = .scaleAspectFill
        return imageView
    }()
    
    // MARK: - Members
    private weak var delegate: CommunityTableViewCellDelegate?
    
    // MARK: - Initialize
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        prepareUI()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        prepareUI()
    }
    
    private func prepareUI() {
        backgroundColor = .clear
        contentView.backgroundColor = .clear
        contentView.isUserInteractionEnabled = true
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(didTapCommunityCell))
        contentView.addGestureRecognizer(tapGesture)
        
        contentView.addSubview(leftIconImageView)
        contentView.addSubview(titleLabel)
        contentView.addSubview(profileImagesStackView)
        contentView.addSubview(rightIconImageView)
        
        NSLayoutConstraint.activate([
            leftIconImageView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
            leftIconImageView.centerYAnchor.constraint(equalTo: titleLabel.centerYAnchor),
            leftIconImageView.widthAnchor.constraint(equalToConstant: 24),
            leftIconImageView.heightAnchor.constraint(equalToConstant: 24),
            
            titleLabel.leadingAnchor.constraint(equalTo: leftIconImageView.trailingAnchor, constant: 8),
            titleLabel.topAnchor.constraint(equalTo: contentView.topAnchor, constant: 16),
            titleLabel.bottomAnchor.constraint(equalTo: contentView.bottomAnchor, constant: -16),
            titleLabel.trailingAnchor.constraint(equalTo: profileImagesStackView.leadingAnchor, constant: -8),
            
            profileImagesStackView.trailingAnchor.constraint(equalTo: rightIconImageView.leadingAnchor, constant: -8),
            profileImagesStackView.centerYAnchor.constraint(equalTo: titleLabel.centerYAnchor),
            
            rightIconImageView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16),
            rightIconImageView.centerYAnchor.constraint(equalTo: titleLabel.centerYAnchor),
            rightIconImageView.widthAnchor.constraint(equalToConstant: 24),
            rightIconImageView.heightAnchor.constraint(equalToConstant: 24)
        ])
    }

    func bind(
        delegate: CommunityTableViewCellDelegate? = nil,
        leftImage: UIImage? = nil,
        title: String = "",
        profileImages: [String] = [],
        rightImage: UIImage? = nil
    ) {
        self.delegate = delegate
        leftIconImageView.image = leftImage
        titleLabel.text = title
        rightIconImageView.image = rightImage
        
        if profileImages.isEmpty {
            profileImagesStackView.isHidden = true
        } else {
            profileImagesStackView.isHidden = false
            setupProfileImages(profileImages)
        }
    }
    
    private func setupProfileImages(_ images: [String]) {
        profileImagesStackView.removeAllArrangedSubviews()
        for image in images {
            let imageView = UIImageView()
            imageView.setImage(with: image, placeholder: Asset.errorUserImage.image)
            imageView.widthAnchor.constraint(equalToConstant: 24).isActive = true
            imageView.heightAnchor.constraint(equalToConstant: 24).isActive = true
            imageView.setCornerRadius(12)
            imageView.clipsToBounds = true
            imageView.contentMode = .scaleAspectFill
            profileImagesStackView.addArrangedSubview(imageView)
        }
    }
    
    @objc private func didTapCommunityCell() {
        delegate?.didTappedCommunityCell()
    }
}
