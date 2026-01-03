//
//  HomeHeaderTableViewCellDelegate.swift
//  Sporthor
//
//  Created by derTurke on 25.06.2025.
//

import UIKit
import ComponentKit

protocol HomeHeaderTableViewCellDelegate: AnyObject {
    func didTappedHomeHeaderIcon(_ tag: Int)
}

extension HomeHeaderTableViewCellDelegate {
    func didTappedHomeHeaderIcon(_ tag: Int) {}
}

final class HomeHeaderTableViewCell: UITableViewCell {
    // MARK: - UI Elements
    private lazy var stackView: CKStackView = {
        let stackView = CKStackView(axis: .horizontal, alignment: .center, spacing: 8)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.distribution = .fill
        return stackView
    }()

    private lazy var menuIconImageView: UIImageView = {
        let imageView = UIImageView(image: Asset.menu.image)
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.widthAnchor.constraint(equalToConstant: 26).isActive = true
        imageView.heightAnchor.constraint(equalToConstant: 26).isActive = true
        imageView.contentMode = .center
        imageView.isUserInteractionEnabled = true
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(didTapMenuIcon))
        imageView.addGestureRecognizer(tapGesture)
        return imageView
    }()

    private lazy var appIconImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFit
        return imageView
    }()

    private lazy var iconStackView: CKStackView = {
        let stackView = CKStackView(axis: .horizontal, alignment: .trailing, spacing: 16)
        return stackView
    }()

    // MARK: - Members
    private weak var delegate: HomeHeaderTableViewCellDelegate?

    // MARK: - Initializers
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        setupUI()
    }

    required init?(coder: NSCoder) {
        super.init(coder: coder)
        setupUI()
    }

    private func setupUI() {
        contentView.addSubview(stackView)
        stackView.addArrangedSubviews([menuIconImageView, appIconImageView, UIView(), iconStackView])

        NSLayoutConstraint.activate([
            stackView.topAnchor.constraint(equalTo: contentView.topAnchor, constant: 8),
            stackView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
            stackView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16),
            stackView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor, constant: -8),
            appIconImageView.widthAnchor.constraint(equalToConstant: 124),
            appIconImageView.heightAnchor.constraint(equalToConstant: 32),
            appIconImageView.centerYAnchor.constraint(equalTo: stackView.centerYAnchor),
            iconStackView.centerYAnchor.constraint(equalTo: stackView.centerYAnchor)
        ])
    }

    // MARK: - Public Methods
    func bind(delegate: HomeHeaderTableViewCellDelegate? = nil,
              appIcon: Icons,
              icons: [Icons]?) {
        self.delegate = delegate
        iconStackView.removeAllArrangedSubviews()
        appIconImageView.image = appIcon.image

        icons?.forEach {
            let imageView = UIImageView()
            imageView.image = $0.image
            imageView.contentMode = .center
            imageView.tag = $0.tag
            imageView.translatesAutoresizingMaskIntoConstraints = false
            imageView.isUserInteractionEnabled = true
            let tapGesture = UITapGestureRecognizer(target: self, action: #selector(didTapIcon(_:)))
            imageView.addGestureRecognizer(tapGesture)
            iconStackView.addArrangedSubview(imageView)

            NSLayoutConstraint.activate([
                imageView.widthAnchor.constraint(equalToConstant: 26),
                imageView.heightAnchor.constraint(equalToConstant: 26),
                imageView.centerYAnchor.constraint(equalTo: iconStackView.centerYAnchor)
            ])
        }
    }

    // MARK: - Actions
    @objc private func didTapIcon(_ gesture: UITapGestureRecognizer) {
        guard let tag = gesture.view?.tag else { return }
        delegate?.didTappedHomeHeaderIcon(tag)
    }

    @objc private func didTapMenuIcon() {
        delegate?.didTappedHomeHeaderIcon(-99)
    }
}
