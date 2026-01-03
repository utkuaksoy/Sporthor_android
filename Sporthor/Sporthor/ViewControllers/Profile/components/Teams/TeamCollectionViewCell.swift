//
//  TeamCollectionViewCell.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 11.02.2025.
//

import ComponentKit
import ComponentBaseKit
import DesignKit
import UIKit

final class TeamCollectionViewCell: UICollectionViewCell, ReusableView {
    
    // MARK: - Private UI Elements
    
    private let containerView: UIView = {
        let view = UIView()
        view.backgroundColor = DesignKitColorName.backgroundWeak100.color
        view.layer.cornerRadius = 20
        view.layer.borderWidth = 1
        view.layer.borderColor = DesignKitColorName.borderSoft200.color.cgColor
        view.layer.masksToBounds = true
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private let iconImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFit
        imageView.layer.cornerRadius = 12
        imageView.clipsToBounds = true
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()
    
    private let titleLabel: CKLabel = {
        let label = CKLabel()
        label.font = .bold04Compact
        label.textColor = DesignKitColorName.borderStrong900.color
        label.numberOfLines = 1
        return label
    }()
    
    private lazy var stackView: UIStackView = {
        let stackView = UIStackView(
            arrangedSubviews: [
                iconImageView,
                titleLabel
            ]
        )
        stackView.axis = .horizontal
        stackView.spacing = 8
        stackView.alignment = .center
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupViews()
        setupConstraints()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
        
    func configure(with image: String?, title: String?) {
        iconImageView.setImage(with: image)
        titleLabel.text = title
    }
}

// MARK: - Setup

private extension TeamCollectionViewCell {
    func setupViews() {
        contentView.addSubview(containerView)
        containerView.addSubview(stackView)
    }
    
    func setupConstraints() {
        NSLayoutConstraint.activate([
            containerView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            containerView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor),
            containerView.topAnchor.constraint(equalTo: contentView.topAnchor),
            containerView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor),
            
            stackView.leadingAnchor.constraint(equalTo: containerView.leadingAnchor, constant: 8),
            stackView.trailingAnchor.constraint(equalTo: containerView.trailingAnchor, constant: -8),
            stackView.topAnchor.constraint(equalTo: containerView.topAnchor),
            stackView.bottomAnchor.constraint(equalTo: containerView.bottomAnchor),
            
            iconImageView.widthAnchor.constraint(equalToConstant: 24),
            iconImageView.heightAnchor.constraint(equalToConstant: 24)
        ])
    }
}
