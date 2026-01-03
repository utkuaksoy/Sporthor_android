//
//  ChatUserInfoActionButtonsView.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 15.04.2025.
//
//

import UIKit
import ComponentKit
import DesignKit

protocol ChatUserInfoActionButtonsViewDelegate: AnyObject {
    func didTapFollowButton()
    func didTapProfileButton()
}

final class ChatUserInfoActionButtonsView: UIView {
    
    // MARK: - Properties
    weak var delegate: ChatUserInfoActionButtonsViewDelegate?
    
    // MARK: - UI Elements
    private lazy var stackView: UIStackView = {
        let stackView = UIStackView()
        stackView.axis = .horizontal
        stackView.distribution = .fillEqually
        stackView.alignment = .fill
        stackView.spacing = 16
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var followButton: CKButton = {
        let button = CKButton(
            title: "Takiptesin",
            titleColor: .black,
            buttonBackgroundColor: .white,
            cornerRadius: 10,
            borderWidth: 1,
            borderColor: ColorName.borderStrong900.color
        )
        button.titleLabel?.font = .bold04Compact
        button.addTarget(self, action: #selector(didTapFollowButton), for: .touchUpInside)
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()
    
    private lazy var profileButton: CKButton = {
        let button = CKButton(
            title: "Profili Görüntüle",
            titleColor: ColorName.contentStrong900.color,
            buttonBackgroundColor: ColorName.backgroundWhite0.color,
            cornerRadius: 10,
            borderWidth: 1,
            borderColor: ColorName.borderStrong900.color
        )
        button.titleLabel?.font = .bold04Compact
        button.addTarget(self, action: #selector(didTapProfileButton), for: .touchUpInside)
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()
    
    // MARK: - Initialization
    init(delegate: ChatUserInfoActionButtonsViewDelegate) {
        self.delegate = delegate
        super.init(frame: .zero)
        setupUI()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    // MARK: - Setup
    private func setupUI() {
        addSubview(stackView)
        stackView.addArrangedSubview(followButton)
        stackView.addArrangedSubview(profileButton)
        
        NSLayoutConstraint.activate([
            stackView.topAnchor.constraint(equalTo: topAnchor),
            stackView.leadingAnchor.constraint(equalTo: leadingAnchor),
            stackView.trailingAnchor.constraint(equalTo: trailingAnchor),
            stackView.bottomAnchor.constraint(equalTo: bottomAnchor),
            stackView.heightAnchor.constraint(equalToConstant: 44)
        ])
    }
    
    // MARK: - Actions

    @objc
    private func didTapFollowButton() {
        delegate?.didTapFollowButton()
    }
    
    @objc
    private func didTapProfileButton() {
        delegate?.didTapProfileButton()
    }
    
    // MARK: - Configuration
    func configure(with viewModel: ChatUserInfoResponse) {
        updateFollowButton(isFollowing: viewModel.isFollow)
    }
    
    func updateFollowButton(isFollowing: Bool) {
        followButton.setTitle(isFollowing ? "Takiptesin" : "Takip Et", for: .normal)
        followButton.backgroundColor = isFollowing ? .white : ColorName.contentStrong900.color
        followButton.setTitleColor(isFollowing ? .black : .white, for: .normal)
        followButton.layer.borderWidth = isFollowing ? 1 : 0
    }
} 
