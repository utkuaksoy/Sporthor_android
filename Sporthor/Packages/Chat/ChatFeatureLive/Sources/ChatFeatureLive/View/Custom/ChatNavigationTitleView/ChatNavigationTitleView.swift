//
//  ChatNavigationTitleView.swift
//  ChatFeatureLive
//
//  Created by Mesut on 29.01.2025.
//

import DesignKit
import Kingfisher
import UIKit

protocol ChatNavigationTitleDelegate: AnyObject {
    func didTapNavigationTitle()
}

final class ChatNavigationTitleView: UIView {
    
    // MARK: - Private UI Elements
    
    private let profileImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFill
        imageView.clipsToBounds = true
        imageView.backgroundColor = .clear
        imageView.layer.cornerRadius = 12
        imageView.clipsToBounds = true
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()
    
    private let usernameLabel: UILabel = {
        let label = UILabel()
        label.font = .heading06
        label.textColor = ColorName.contentStrong900.color
        return label
    }()
    
    private lazy var stackView: UIStackView = {
        let stackView = UIStackView(arrangedSubviews: [profileImageView, usernameLabel])
        stackView.axis = .horizontal
        stackView.spacing = 8
        stackView.alignment = .center
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(didTapNavigationTitle))
        stackView.addGestureRecognizer(tapGesture)
        return stackView
    }()
    
    // MARK: - Properties

    weak var delegate: ChatNavigationTitleDelegate?
    
    deinit {
        print("\(self) deinit ✅")
    }
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupView()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    private func setupView() {
        addSubview(stackView)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        
        NSLayoutConstraint.activate([
            profileImageView.widthAnchor.constraint(equalToConstant: 24),
            profileImageView.heightAnchor.constraint(equalToConstant: 24),
            
            stackView.leadingAnchor.constraint(equalTo: leadingAnchor),
            stackView.topAnchor.constraint(equalTo: topAnchor),
            stackView.bottomAnchor.constraint(equalTo: bottomAnchor),
            stackView.trailingAnchor.constraint(lessThanOrEqualTo: trailingAnchor)
        ])
    }
    
    func configure(
        username: String,
        profileImage: String?
    ) {
        usernameLabel.text = username
        profileImageView.setImage(with: profileImage, placeholder: .errorUserImage)
    }
    
    @objc
    private func didTapNavigationTitle() {
        delegate?.didTapNavigationTitle()
    }
}
