//
//  ProfileInfoCollectionViewCell.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 9.02.2025.
//

import ComponentKit
import ComponentBaseKit
import DesignKit
import UIKit

public protocol ProfileInfoCellDelegate: AnyObject {
    func didTapFollowerView()
    func didTapFollowingView()
    func didTapPostStatView()
}

final class ProfileInfoCollectionViewCell: UICollectionViewCell, ReusableView, ComponentDisplayer, ComponentDisplayerViewModelConfigurable {
    
    // MARK: - Private UI Elements
    
    private lazy var containerView: UIView = {
        let view = UIView()
        view.backgroundColor = .white
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var profileImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFill
        imageView.layer.cornerRadius = 36
        imageView.layer.masksToBounds = true
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()
    
    private lazy var usernameLabel: CKLabel = {
        let label = CKLabel()
        label.font = .heading07
        label.textColor = DesignKitColorName.borderStrong900.color
        label.textAlignment = .center
        return label
    }()
    
    private lazy var postStatView: ProfileStatView = {
        let view = ProfileStatView()
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(didTapPostView))
        view.isUserInteractionEnabled = true
        view.addGestureRecognizer(tapGesture)
        return view
    }()
    
    private lazy var followerStatView: ProfileStatView = {
        let view = ProfileStatView()
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(didTapFollowerView))
        view.isUserInteractionEnabled = true
        view.addGestureRecognizer(tapGesture)
        return view
    }()

    private lazy var followingStatView: ProfileStatView = {
        let view = ProfileStatView()
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(didTapFollowingView))
        view.isUserInteractionEnabled = true
        view.addGestureRecognizer(tapGesture)
        return view
    }()
    
    private lazy var statsStackView: UIStackView = {
        let stackView = UIStackView(arrangedSubviews: [
            postStatView,
            followerStatView,
            followingStatView
        ])
        stackView.axis = .horizontal
        stackView.alignment = .center
        stackView.spacing = 16
        stackView.distribution = .equalSpacing
        return stackView
    }()
    
    private lazy var mainStackView: UIStackView = {
        let stackView = UIStackView(arrangedSubviews: [
            profileImageView,
            nameAndStatStackView
        ])
        stackView.axis = .horizontal
        stackView.alignment = .center
        stackView.spacing = 12
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var nameAndStatStackView: UIStackView = {
        let stackView = UIStackView(arrangedSubviews: [
            usernameLabel,
            statsStackView
        ])
        stackView.axis = .vertical
        stackView.alignment = .leading
        stackView.spacing = 4
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    // MARK: - Private Properties
    
    private var viewModel: ProfileInfoComponentViewModel?
    private weak var delegate: ProfileInfoCellDelegate?
    
    // MARK: - Initializers
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupUI()
        setupConstraints()
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        setupUI()
        setupConstraints()
    }
    
    // MARK: - Public Configure Method
    
    func configure(
        with viewModel: ProfileInfoComponentViewModel,
        at indexPath: IndexPath,
        delegate: ProfileInfoCellDelegate?
    ) {
        self.delegate = delegate
        self.viewModel = viewModel
        configureUsernameLabel(with: viewModel.profileName)
        configureProfileImageView(with: viewModel.profileImage)
        postStatView.configure(count: viewModel.postCount, label: "Gönderi")
        followerStatView.configure(count: viewModel.follewerCount, label: "Takipçi")
        followingStatView.configure(count: viewModel.followingCount, label: "Takip")
    }
    
    // MARK: - Private Configure Methods
    
    private func configureProfileImageView(with image: String?) {
        profileImageView.setImage(with: image, placeholder: Asset.errorUserImage.image)
    }
    
    private func configureUsernameLabel(with name: String?) {
        usernameLabel.text = name
    }
    
    @objc
    private func didTapFollowerView() {
        delegate?.didTapFollowerView()
    }
    
    @objc
    private func didTapFollowingView() {
        delegate?.didTapFollowingView()
    }
    
    @objc
    private func didTapPostView() {
        delegate?.didTapPostStatView()
    }
}

// MARK: - Setup

private extension ProfileInfoCollectionViewCell {
    func setupUI() {
        contentView.addSubview(containerView)
        containerView.addSubview(mainStackView)
    }
    
    func setupConstraints() {
        NSLayoutConstraint.activate([
            containerView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            containerView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor),
            containerView.topAnchor.constraint(equalTo: contentView.topAnchor),
            containerView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor),
           
            mainStackView.topAnchor.constraint(equalTo: containerView.topAnchor),
            mainStackView.leadingAnchor.constraint(equalTo: containerView.leadingAnchor, constant: 16),
            mainStackView.trailingAnchor.constraint(equalTo: containerView.trailingAnchor, constant: -16),
            mainStackView.bottomAnchor.constraint(equalTo: containerView.bottomAnchor),
            profileImageView.widthAnchor.constraint(equalToConstant: 72),
            profileImageView.heightAnchor.constraint(equalToConstant: 72),
        ])
    }
}
