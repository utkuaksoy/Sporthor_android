//
//  ProfileActionButtonsCell.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 11.02.2025.
//

import ComponentBaseKit
import DesignKit
import UIKit

public protocol ProfileActionButtonsCellDelegate: AnyObject {
    func didTapButton(ofType type: ProfileActionButtonType)
}

final class ProfileActionButtonsCell: UICollectionViewCell, ReusableView, ComponentDisplayer, ComponentDisplayerViewModelConfigurable {
    
    // MARK: - Private UI Elements
    
    private let containerView: UIView = {
        let view = UIView()
        view.backgroundColor = .white
        view.layer.cornerRadius = 20
        view.layer.borderWidth = 1
        view.layer.borderColor = UIColor.clear.cgColor
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var buttonStackView: UIStackView = {
        let stackView = UIStackView()
        stackView.axis = .horizontal
        stackView.spacing = 12
        stackView.distribution = .fillEqually
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private var buttons: [UIButton] = []
    
    // MARK: - Private Properties
    
    private var viewModel: ProfileActionButtonsComponentViewModel?
    private weak var delegate: ProfileActionButtonsCellDelegate?
    
    // MARK: - Initializer
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupViews()
        setupConstraints()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    // MARK: - Configure
    
    func configure(
        with viewModel: ProfileActionButtonsComponentViewModel,
        at indexPath: IndexPath,
        delegate: ProfileActionButtonsCellDelegate?
    ) {
        self.viewModel = viewModel
        self.delegate = delegate
        configureActionButtons()
    }
    
    // MARK: - Private Configure Methods
    
    private func configureActionButtons() {
        guard let viewModel, let buttonTypes = viewModel.data.buttons else { return }
        buttons.forEach { $0.removeFromSuperview() }
        buttons.removeAll()
        
        for actionType in buttonTypes {
            let button = createButton(for: actionType)
            buttons.append(button)
            buttonStackView.addArrangedSubview(button)
        }
    }
    
    private func createButton(for type: ProfileActionButtonType) -> UIButton {
        let button = UIButton()
        button.setTitle(type.title, for: .normal)
        button.setTitleColor(DesignKitColorName.borderStrong900.color, for: .normal)
        button.titleLabel?.font = .bold04Compact
        button.backgroundColor = type.backgroundColor
        button.layer.cornerRadius = 10
        button.layer.borderWidth = type.isBorder ? 1 : 0
        button.layer.borderColor = type.isBorder ? DesignKitColorName.borderStrong900.color.cgColor : nil
        button.addTarget(self, action: #selector(buttonTapped(_:)), for: .touchUpInside)
        button.tag = ProfileActionButtonType.allCases.firstIndex(of: type) ?? 0
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }
    
    @objc
    private func buttonTapped(_ sender: UIButton) {
        guard let buttonType = ProfileActionButtonType.allCases[safe: sender.tag] else { return }
        delegate?.didTapButton(ofType: buttonType)
        switch buttonType {
        case .follow, .following, .followRequestSent:
            updateButtons()
            viewModel?.toggleFollowStatus()
        default: break
        }
    }
    
    private func updateButtons() {
        configureActionButtons()
    }
}

// MARK: - Setup

private extension ProfileActionButtonsCell {
    func setupViews() {
        contentView.addSubview(containerView)
        containerView.addSubview(buttonStackView)
    }
    
    func setupConstraints() {
        NSLayoutConstraint.activate([
            containerView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            containerView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor),
            containerView.topAnchor.constraint(equalTo: contentView.topAnchor),
            containerView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor),
            
            buttonStackView.leadingAnchor.constraint(equalTo: containerView.leadingAnchor, constant: 16),
            buttonStackView.trailingAnchor.constraint(equalTo: containerView.trailingAnchor, constant: -16),
            buttonStackView.topAnchor.constraint(equalTo: containerView.topAnchor),
            buttonStackView.bottomAnchor.constraint(equalTo: containerView.bottomAnchor)
        ])
    }
}
