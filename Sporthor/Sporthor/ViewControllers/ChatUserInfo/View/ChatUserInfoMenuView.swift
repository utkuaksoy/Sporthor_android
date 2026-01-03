//
//  ChatUserInfoMenuView.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 15.04.2025.
//
//

import UIKit
import DesignKit

protocol ChatUserInfoMenuViewDelegate: AnyObject {
    func didTapMediaButton()
    func didTapClearChatButton()
}

final class ChatUserInfoMenuView: UIView {

    // MARK: - Private UI Elements

    private lazy var stackView: UIStackView = {
        let stackView = UIStackView()
        stackView.axis = .vertical
        stackView.spacing = 0
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var mediaButton: UIButton = {
        let button = createMenuButton(title: "Medya ve belgeler", icon: Asset.mediaImage.image, countLabel: &mediaCountLabel)
        button.addTarget(self, action: #selector(didTapMediaButton), for: .touchUpInside)
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()
    
    private lazy var mediaSeparatorView: UIView = {
        let view = UIView()
        view.backgroundColor = ColorName.borderSoft200.color
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var clearChatButton: UIButton = {
        let button = createMenuButton(title: "Sohbeti temizle", icon: Asset.redTrashIcon.image)
        button.addTarget(self, action: #selector(didTapClearChatButton), for: .touchUpInside)
        button.isHidden = true
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()
    
    private lazy var clearChatSeparatorView: UIView = {
        let view = UIView()
        view.backgroundColor = ColorName.borderSoft200.color
        view.translatesAutoresizingMaskIntoConstraints = false
        view.isHidden = true
        return view
    }()
    
    // MARK: - Properties
    
    weak var delegate: ChatUserInfoMenuViewDelegate?
    private var mediaCountLabel: UILabel?
    
    // MARK: - Initialization
    
    init(delegate: ChatUserInfoMenuViewDelegate) {
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
        
        stackView.addArrangedSubview(mediaButton)
        stackView.addArrangedSubview(mediaSeparatorView)
        stackView.addArrangedSubview(clearChatButton)
        stackView.addArrangedSubview(clearChatSeparatorView)
        
        NSLayoutConstraint.activate([
            stackView.topAnchor.constraint(equalTo: topAnchor),
            stackView.leadingAnchor.constraint(equalTo: leadingAnchor),
            stackView.trailingAnchor.constraint(equalTo: trailingAnchor),
            stackView.bottomAnchor.constraint(equalTo: bottomAnchor),
            
            mediaSeparatorView.heightAnchor.constraint(equalToConstant: 1),
            clearChatSeparatorView.heightAnchor.constraint(equalToConstant: 1)
        ])
    }
    
    // MARK: - Actions
    
    @objc
    private func didTapMediaButton() {
        delegate?.didTapMediaButton()
    }
    
    @objc
    private func didTapClearChatButton() {
        delegate?.didTapClearChatButton()
    }
    
    // MARK: - Configuration
    
    func configure(with viewModel: ChatUserInfoResponse) {
        mediaCountLabel?.text = "\(viewModel.mediaCount)"
    }
    
    // MARK: - Private Methods

    private func createMenuButton(title: String, icon: UIImage) -> UIButton {
        var dummyLabel: UILabel? = nil
        return createMenuButton(title: title, icon: icon, countLabel: &dummyLabel)
    }

    private func createMenuButton(title: String, icon: UIImage, countLabel: inout UILabel?) -> UIButton {
        let button = UIButton()
        button.backgroundColor = .clear
        button.translatesAutoresizingMaskIntoConstraints = false
        button.heightAnchor.constraint(equalToConstant: 56).isActive = true

        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFit
        imageView.tintColor = ColorName.contentStrong900.color
        imageView.image = icon
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.widthAnchor.constraint(equalToConstant: 24).isActive = true
        imageView.heightAnchor.constraint(equalToConstant: 24).isActive = true
        imageView.isUserInteractionEnabled = false

        let titleLabel = UILabel()
        titleLabel.text = title
        titleLabel.font = .bold04Compact
        titleLabel.textColor = ColorName.contentStrong900.color
        titleLabel.translatesAutoresizingMaskIntoConstraints = false
        titleLabel.setContentHuggingPriority(.defaultLow, for: .horizontal)
        titleLabel.setContentCompressionResistancePriority(.defaultLow, for: .horizontal)
        titleLabel.isUserInteractionEnabled = false

        let countLbl = UILabel()
        countLbl.font = .body04Compact
        countLbl.textColor = ColorName.contentSoft600.color
        countLbl.translatesAutoresizingMaskIntoConstraints = false
        countLbl.setContentHuggingPriority(.required, for: .horizontal)
        countLbl.setContentCompressionResistancePriority(.required, for: .horizontal)
        countLbl.isUserInteractionEnabled = false
        countLabel = countLbl

        let chevronImageView = UIImageView()
        chevronImageView.contentMode = .scaleAspectFit
        chevronImageView.tintColor = .gray
        chevronImageView.image = Asset.chevronRight.image
        chevronImageView.translatesAutoresizingMaskIntoConstraints = false
        chevronImageView.widthAnchor.constraint(equalToConstant: 24).isActive = true
        chevronImageView.heightAnchor.constraint(equalToConstant: 24).isActive = true
        chevronImageView.isUserInteractionEnabled = false

        let contentStack = UIStackView(arrangedSubviews: [imageView, titleLabel, countLbl])
        contentStack.axis = .horizontal
        contentStack.spacing = 8
        contentStack.alignment = .center
        contentStack.setContentHuggingPriority(.defaultHigh, for: .horizontal)
        contentStack.translatesAutoresizingMaskIntoConstraints = false
        contentStack.isUserInteractionEnabled = false

        let mainStack = UIStackView(arrangedSubviews: [contentStack, chevronImageView])
        mainStack.axis = .horizontal
        mainStack.spacing = 8
        mainStack.alignment = .center
        mainStack.translatesAutoresizingMaskIntoConstraints = false
        mainStack.distribution = .fill
        mainStack.isUserInteractionEnabled = false

        button.addSubview(mainStack)

        NSLayoutConstraint.activate([
            mainStack.topAnchor.constraint(equalTo: button.topAnchor),
            mainStack.bottomAnchor.constraint(equalTo: button.bottomAnchor),
            mainStack.leadingAnchor.constraint(equalTo: button.leadingAnchor),
            mainStack.trailingAnchor.constraint(equalTo: button.trailingAnchor)
        ])

        return button
    }
}
