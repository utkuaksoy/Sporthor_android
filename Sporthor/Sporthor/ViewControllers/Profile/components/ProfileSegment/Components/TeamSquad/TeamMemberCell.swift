//
//  TeamMemberCell.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 16.03.2025.
//

import ComponentKit
import ComponentBaseKit
import DesignKit
import UIKit

final class TeamMemberCell: UICollectionViewCell, ReusableView {
    
    // MARK: - Private UI Elements

    private lazy var profileImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFill
        imageView.clipsToBounds = true
        imageView.layer.cornerRadius = 24
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()

    private lazy var nameLabel: CKLabel = {
        let label = CKLabel(
            textColor: ColorName.contentStrong900.color,
            font: .bold04Compact
        )
        return label
    }()

    private lazy var roleLabel: CKLabel = {
        let label = CKLabel(
            textColor: ColorName.contentSoft600.color,
            font: .interTight400
        )
        return label
    }()

    private let followButton: CKButton = {
        let button = CKButton(
            titleColor: ColorName.contentStrong900.color,
            buttonBackgroundColor: .white,
            cornerRadius: 10,
            borderWidth: 1,
            borderColor: ColorName.borderStrong900.color,
            font: .bold04Compact
        )
        button.addTarget(self, action: #selector(followButtonTapped), for: .touchUpInside)
        return button
    }()
    
    private var isFollowing: Bool = false {
        didSet {
            updateFollowButton()
        }
    }
    
    private lazy var textStackView: CKStackView = {
        let stackView = CKStackView(
            axis: .vertical,
            distribution: .fill,
            alignment: .fill,
            spacing: 2
        )
        stackView.addArrangedSubviews([nameLabel, roleLabel])
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var mainStackView: CKStackView = {
        let stackView = CKStackView(
            axis: .horizontal,
            distribution: .fill,
            alignment: .center,
            spacing: 8
        )
        stackView.addArrangedSubviews([profileImageView, textStackView, followButton])
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

    func configure(with model: TeamMemberItem?) {
        nameLabel.text = model?.name
        roleLabel.text = model?.role
        isFollowing = model?.isFollowing ?? false
        
        if let imageUrl = model?.imageURL {
            profileImageView.setImage(with: imageUrl)
        }
    }
    
    private func updateFollowButton() {
        if isFollowing {
            followButton.setTitle("Takiptesin", for: .normal)
            followButton.backgroundColor = .white
            followButton.setTitleColor(ColorName.contentStrong900.color, for: .normal)
        } else {
            followButton.setTitle("Takip Et", for: .normal)
            followButton.backgroundColor = ColorName.contentStrong900.color
            followButton.setTitleColor(.white, for: .normal)
        }
    }

    // MARK: - Actions
    @objc
    private func followButtonTapped() {
        isFollowing.toggle()
    }
}

// MARK: - Setup

private extension TeamMemberCell {
    func setupViews() {
        contentView.backgroundColor = .white
        contentView.addSubview(mainStackView)
    }

    func setupConstraints() {
        NSLayoutConstraint.activate([
            profileImageView.widthAnchor.constraint(equalToConstant: 48),
            profileImageView.heightAnchor.constraint(equalToConstant: 48),
            
            followButton.widthAnchor.constraint(equalToConstant: 93),
            followButton.heightAnchor.constraint(equalToConstant: 30),
            
            mainStackView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            mainStackView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor),
            mainStackView.topAnchor.constraint(equalTo: contentView.topAnchor),
            mainStackView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor)
        ])
    }
}
