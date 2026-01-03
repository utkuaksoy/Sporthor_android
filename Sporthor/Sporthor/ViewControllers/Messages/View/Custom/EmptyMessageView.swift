//
//  EmptyMessageView.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 26.03.2025.
//

import DesignKit
import UIKit

final class EmptyMessageView: UIView {

    // MARK: - Private UI Elements
    
    private lazy var containerView: UIView = {
        let view = UIView()
        view.backgroundColor = ColorName.backgroundWhite0.color
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var emptyImageContainerView: UIView = {
        let view = UIView()
        view.backgroundColor = ColorName.backgroundWeak100.color
        view.layer.cornerRadius = 36
        view.clipsToBounds = true
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()

    private let emptyImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.tintColor = ColorName.backgroundWeak100.color
        imageView.contentMode = .scaleAspectFit
        imageView.image = .emptyMessageIcon
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()
    
    private let emptyTitleLabel: UILabel = {
        let label = UILabel()
        label.textColor = ColorName.contentSoft600.color
        label.font = .heading05
        label.textAlignment = .center
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var stackView: UIStackView = {
        let stack = UIStackView(arrangedSubviews: [emptyImageContainerView, emptyTitleLabel])
        stack.axis = .vertical
        stack.alignment = .center
        stack.spacing = 8
        stack.translatesAutoresizingMaskIntoConstraints = false
        return stack
    }()
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupViews()
        setupConstraints()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    func configure(with title: String) {
        emptyTitleLabel.text = title
    }
}

private extension EmptyMessageView {
    
    func setupViews() {
        addSubview(containerView)
        containerView.addSubview(stackView)
        emptyImageContainerView.addSubview(emptyImageView)
    }
    
    func setupConstraints() {
        NSLayoutConstraint.activate([
            
            containerView.leadingAnchor.constraint(equalTo: leadingAnchor),
            containerView.trailingAnchor.constraint(equalTo: trailingAnchor),
            containerView.topAnchor.constraint(equalTo: topAnchor),
            containerView.bottomAnchor.constraint(equalTo: bottomAnchor),
            
            stackView.topAnchor.constraint(equalTo: containerView.topAnchor, constant: 65),
            stackView.leadingAnchor.constraint(equalTo: containerView.leadingAnchor, constant: 32),
            stackView.trailingAnchor.constraint(equalTo: containerView.trailingAnchor, constant: -32),
            stackView.heightAnchor.constraint(equalToConstant: 150),
            
            emptyImageContainerView.widthAnchor.constraint(equalToConstant: 72),
            emptyImageContainerView.heightAnchor.constraint(equalToConstant: 72),
            
            emptyImageView.widthAnchor.constraint(equalToConstant: 32),
            emptyImageView.heightAnchor.constraint(equalToConstant: 32),
            emptyImageView.centerXAnchor.constraint(equalTo: emptyImageContainerView.centerXAnchor),
            emptyImageView.centerYAnchor.constraint(equalTo: emptyImageContainerView.centerYAnchor)
        ])
    }
}
