//
//  FollowerCollectionViewCell.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 02.04.2025.
//

import ComponentKit
import ComponentBaseKit
import DesignKit
import UIKit

protocol FollowerCellDelegate: AnyObject {
    func followButtonTapped(at indexPath: IndexPath)
}

final class FollowerCollectionViewCell: UICollectionViewCell, ReusableView {
    
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
        button.addTarget(self, action: #selector(followButtonAction), for: .touchUpInside)
        return button
    }()
    
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
    
    // MARK: - Properties

    private weak var delegate: FollowerCellDelegate?
    private var indexPath: IndexPath?
    private var model: CKFollowerCellModel?
    
    // MARK: - Initialization

    override init(frame: CGRect) {
        super.init(frame: frame)
        setupViews()
        setupConstraints()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func prepareForReuse() {
        super.prepareForReuse()
        profileImageView.image = nil
        nameLabel.text = nil
        roleLabel.text = nil
        followButton.isHidden = true
    }
    
    // MARK: - Configuration
    func configure(
        with model: CKFollowerCellModel,
        delegate: FollowerCellDelegate?,
        at indexPath: IndexPath
    ) {
        self.model = model
        self.delegate = delegate
        self.indexPath = indexPath
        configureNameLabel(with: model.name)
        configureProfileImageView(with: model.imageUrl)
        configureRoleLabel(with: model.summary)
        configureFollowButton(model: model)
    }
    
    private func configureNameLabel(with name: String) {
        nameLabel.text = name
    }
    
    private func configureProfileImageView(with image: String?) {
        profileImageView.setImage(with: image, placeholder: Asset.errorUserImage.image)
    }
    
    private func configureRoleLabel(with role: String) {
        roleLabel.text = role
    }
    
    private func configureFollowButton(model: CKFollowerCellModel) {
        followButton.isHidden = model.isCurrentUser
        if !model.isCurrentUser {
            let style = model.followState.style
            followButton.setTitle(model.followState.title, for: .normal)
            followButton.backgroundColor = style.backgroundColor
            followButton.setTitleColor(style.titleColor, for: .normal)
            followButton.layer.borderColor = style.borderColor.cgColor
        }
    }
    
    // MARK: - Actions
    @objc
    private func followButtonAction() {
        guard let indexPath else { return }
        if model?.followState == .following {
            showUnfollowActionSheet()
        } else {
            delegate?.followButtonTapped(at: indexPath)
        }
    }
    
    private func showUnfollowActionSheet() {
        guard let indexPath else { return }
        let actionSheet = UIAlertController(title: nil, message: nil, preferredStyle: .actionSheet)
        
        let unfollowAction = UIAlertAction(title: "Takibi Bırak", style: .destructive) { [weak self] _ in
            guard let self = self else { return }
            self.delegate?.followButtonTapped(at: indexPath)
        }
        
        let cancelAction = UIAlertAction(title: "İptal", style: .cancel)
        
        actionSheet.addAction(unfollowAction)
        actionSheet.addAction(cancelAction)
        
        if let viewController = window?.rootViewController {
            viewController.present(actionSheet, animated: true)
        }
    }
} 

// MARK: - Setup

private extension FollowerCollectionViewCell {
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
            
            mainStackView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
            mainStackView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16),
            mainStackView.topAnchor.constraint(equalTo: contentView.topAnchor),
            mainStackView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor)
        ])
    }
}
