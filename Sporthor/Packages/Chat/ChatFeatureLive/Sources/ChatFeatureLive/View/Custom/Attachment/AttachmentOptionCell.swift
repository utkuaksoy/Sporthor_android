//
//  AttachmentOptionCell.swift
//  ChatFeatureLive
//
//  Created by Mesut Canbaz on 22.03.2025.
//

import ComponentBaseKit
import DesignKit
import InputBarAccessoryView
import UIKit

final class AttachmentOptionCell: UICollectionViewCell, ReusableView {

    private lazy var containerView: UIView = {
        let view = UIView()
        view.backgroundColor = ColorName.backgroundWeak100.color
        view.layer.cornerRadius = 8
        view.layer.borderWidth = 1
        view.layer.borderColor = ColorName.borderSoft200.color.cgColor
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()

    private lazy var iconImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFit
        imageView.tintColor = ColorName.contentStrong900.color
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.widthAnchor.constraint(equalToConstant: 24).isActive = true
        imageView.heightAnchor.constraint(equalToConstant: 24).isActive = true
        return imageView
    }()

    private lazy var titleLabel: UILabel = {
        let label = UILabel()
        label.textColor = ColorName.contentStrong900.color
        label.font = .bold06Compact
        label.textAlignment = .center
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()

    private lazy var verticalStack: UIStackView = {
        let stack = UIStackView(arrangedSubviews: [iconImageView, titleLabel])
        stack.axis = .vertical
        stack.alignment = .center
        stack.spacing = 8
        stack.translatesAutoresizingMaskIntoConstraints = false
        return stack
    }()

    override init(frame: CGRect) {
        super.init(frame: frame)
        setupViews()
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    private func setupViews() {
        contentView.addSubview(containerView)
        containerView.addSubview(verticalStack)

        NSLayoutConstraint.activate([
            containerView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            containerView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor),
            containerView.topAnchor.constraint(equalTo: contentView.topAnchor),
            containerView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor),

            verticalStack.centerXAnchor.constraint(equalTo: containerView.centerXAnchor),
            verticalStack.centerYAnchor.constraint(equalTo: containerView.centerYAnchor),
            verticalStack.leadingAnchor.constraint(greaterThanOrEqualTo: containerView.leadingAnchor, constant: 8),
            verticalStack.trailingAnchor.constraint(lessThanOrEqualTo: containerView.trailingAnchor, constant: -8)
        ])
    }

    func configure(icon: UIImage?, title: String) {
        iconImageView.image = icon
        titleLabel.text = title
    }
}
