//
//  ProfileTabItemCell.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 14.03.2025.
//

import ComponentKit
import ComponentBaseKit
import DesignKit
import UIKit

final class ProfileTabItemCell: UICollectionViewCell, ReusableView {
    
    // MARK: - Private UI Elements
    
    private lazy var imageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFit
        imageView.tintColor = ColorName.contentSoft600.color
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()
    
    private lazy var titleLabel: CKLabel = {
        let label = CKLabel(
            textColor: ColorName.contentSoft600.color,
            font: .bold04Compact
        )
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var stackView: UIStackView = {
        let stackView = UIStackView(arrangedSubviews: [imageView, titleLabel])
        stackView.axis = .horizontal
        stackView.spacing = 8
        stackView.alignment = .center
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    // MARK: - Initializer
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupViews()
        setupConstraints()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    // MARK: - Configure Methods

    func configure(title: String?, image: String?, isSelected: Bool) {
        configureTitleLabel(with: title, isSelected: isSelected)
        configureImageView(with: image, isSelected: isSelected)
    }
    
    private func configureTitleLabel(with title: String?, isSelected: Bool) {
        if let title {
            titleLabel.text = title
            titleLabel.textColor = isSelected ? ColorName.contentStrong900.color : ColorName.contentSoft600.color
        } else {
            titleLabel.isHidden = true
        }
    }
    
    private func configureImageView(with image: String?, isSelected: Bool) {
        if let image {
            imageView.setImage(with: image)
            imageView.tintColor = isSelected ? ColorName.contentStrong900.color : ColorName.contentSoft600.color
        } else {
            imageView.isHidden = true
        }
    }
    
}

// MARK: - Setup

private extension ProfileTabItemCell {
    func setupViews() {
        contentView.addSubview(stackView)
    }
    
    func setupConstraints() {
        NSLayoutConstraint.activate([
            stackView.centerXAnchor.constraint(equalTo: contentView.centerXAnchor),
            stackView.centerYAnchor.constraint(equalTo: contentView.centerYAnchor),
            
            imageView.widthAnchor.constraint(equalToConstant: 24),
            imageView.heightAnchor.constraint(equalToConstant: 24)
        ])
    }
}
