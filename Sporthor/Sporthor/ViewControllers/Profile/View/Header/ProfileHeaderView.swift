//
//  ProfileHeaderView.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 11.02.2025.
//

import UIKit

protocol ProfileHeaderViewDelegate: AnyObject {
    func didTapSettingsButton()
}

final class ProfileHeaderView: UIView {
    
    // MARK: - Private UI Elements
    
    private lazy var usernameLabel: UILabel = {
        let label = UILabel()
        label.text = "bade.belgin"
        label.font = .heading06
        label.textColor = DesignKitColorName.borderStrong900.color
        label.setContentHuggingPriority(.required, for: .horizontal)
        label.setContentCompressionResistancePriority(.required, for: .horizontal)
        return label
    }()
    
    private lazy var dropdownImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.image = UIImage(systemName: "chevron.down")
        imageView.tintColor = DesignKitColorName.borderStrong900.color
        imageView.contentMode = .scaleAspectFit
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()
    
    private lazy var settingsButtonContanerView: UIView = {
        let view = UIView()
        view.backgroundColor = .clear
        return view
    }()
    
    private lazy var settingsButton: UIButton = {
        let button = UIButton()
        let image = UIImage(systemName: "gearshape")?.withRenderingMode(.alwaysTemplate)
        button.setImage(image, for: .normal)
        button.tintColor = DesignKitColorName.borderStrong900.color
        button.translatesAutoresizingMaskIntoConstraints = false
        button.addTarget(self, action: #selector(didTapSettingsButton), for: .touchUpInside)
        return button
    }()
    
    private lazy var userStackView: UIStackView = {
        let stackView = UIStackView(arrangedSubviews: [usernameLabel, dropdownImageView])
        stackView.axis = .horizontal
        stackView.spacing = 4
        stackView.alignment = .center
        return stackView
    }()
    
    private lazy var mainStackView: UIStackView = {
        let stackView = UIStackView(arrangedSubviews: [userStackView, settingsButtonContanerView])
        stackView.axis = .horizontal
        stackView.spacing = 16
        stackView.alignment = .fill
        stackView.distribution = .fill
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    // MARK: - Private Properties
    private weak var delegate: ProfileHeaderViewDelegate?
    
    // MARK: Initializers
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupViews()
        setupConstraints()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    //MARK: Configure Methods
    
    func configure(delegate: ProfileHeaderViewDelegate?, userName: String) {
        self.delegate = delegate
        usernameLabel.text = userName
    }
    
    @objc
    private func didTapSettingsButton() {
        delegate?.didTapSettingsButton()
    }
    
}

// MARK: Setup

private extension ProfileHeaderView {
    func setupViews() {
        addSubview(mainStackView)
        settingsButtonContanerView.addSubview(settingsButton)
    }
    
    func setupConstraints() {
        NSLayoutConstraint.activate([
            mainStackView.leadingAnchor.constraint(equalTo: leadingAnchor, constant: 16),
            mainStackView.trailingAnchor.constraint(equalTo: trailingAnchor, constant: -16),
            mainStackView.topAnchor.constraint(equalTo: topAnchor, constant: 8),
            mainStackView.bottomAnchor.constraint(equalTo: bottomAnchor),
            
            dropdownImageView.widthAnchor.constraint(equalToConstant: 16),
            dropdownImageView.heightAnchor.constraint(equalToConstant: 16),
            
            settingsButton.trailingAnchor.constraint(equalTo: settingsButtonContanerView.trailingAnchor),
            settingsButton.widthAnchor.constraint(equalToConstant: 24),
            settingsButton.heightAnchor.constraint(equalToConstant: 24),
            settingsButton.centerYAnchor.constraint(equalTo: settingsButtonContanerView.centerYAnchor)
        ])
    }
}
