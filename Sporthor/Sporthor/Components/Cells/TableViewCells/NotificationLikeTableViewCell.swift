//
//  NotificationLikeTableViewCell.swift
//  Sporthor
//
//  Created by derTurke on 29.06.2025.
//

import UIKit
import ComponentKit

final class NotificationLikeTableViewCell: UITableViewCell {
    // MARK: - UI Elements
    private lazy var likeImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFill
        imageView.clipsToBounds = true
        imageView.setCornerRadius(8)
        imageView.heightAnchor.constraint(equalToConstant: 48).isActive = true
        imageView.widthAnchor.constraint(equalToConstant: 48).isActive = true
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()
    
    private lazy var likeIconImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFit
        imageView.heightAnchor.constraint(equalToConstant: 22).isActive = true
        imageView.widthAnchor.constraint(equalToConstant: 22).isActive = true
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()
    
    private lazy var titleLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentStrong900.color,
                            numberOfLines: 0,
                            font: .bold04Compact)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var descriptionLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentSub800.color,
                            numberOfLines: 0,
                            font: .body04Compact)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var labelStackView: CKStackView = {
        let stackView = CKStackView(spacing: 4)
        stackView.addArrangedSubviews([titleLabel, descriptionLabel])
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var separatorView: CKSeparatorView = {
        let view = CKSeparatorView()
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    // MARK: - Initializers
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        prepareUI()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        prepareUI()
    }
    
    private func prepareUI() {
        selectionStyle = .none
        backgroundColor = .clear
        contentView.backgroundColor = .clear
        
        contentView.addSubview(labelStackView)
        contentView.addSubview(likeImageView)
        contentView.addSubview(likeIconImageView)
        contentView.addSubview(separatorView)
        
        NSLayoutConstraint.activate([
            likeImageView.topAnchor.constraint(equalTo: contentView.topAnchor, constant: 16),
            likeImageView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
            likeImageView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor, constant: -16),
            
            likeIconImageView.bottomAnchor.constraint(equalTo: likeImageView.bottomAnchor, constant: 8),
            likeIconImageView.trailingAnchor.constraint(equalTo: likeImageView.trailingAnchor, constant: 11),
            
            labelStackView.leadingAnchor.constraint(equalTo: likeImageView.trailingAnchor, constant: 16),
            labelStackView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16),
            labelStackView.centerYAnchor.constraint(equalTo: likeImageView.centerYAnchor),
            
            separatorView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor),
            separatorView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            separatorView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor)
        ])
    }
    
    func configure(model: NotificationModel) {
        likeImageView.setImage(with: model.image)
        likeIconImageView.image = Asset.notificationLike.image
        titleLabel.text = model.title
        titleLabel.isHidden = model.title.isEmpty
        descriptionLabel.text = model.message
    }
}
