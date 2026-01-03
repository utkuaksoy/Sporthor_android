//
//  NotificationTrainingGroupRequestInfoTableViewCell.swift
//  Sporthor
//
//  Created by derTurke on 30.06.2025.
//

import UIKit
import ComponentKit

protocol NotificationTrainingGroupRequestInfoTableViewCellDelegate: AnyObject {
    func didTappedImageTrainingGroupRequestInfoInfo(model: NotificationModel)
}

final class NotificationTrainingGroupRequestInfoTableViewCell: UITableViewCell {
    // MARK: - UI Elements
    private lazy var groupImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFill
        imageView.heightAnchor.constraint(equalToConstant: 40).isActive = true
        imageView.widthAnchor.constraint(equalToConstant: 40).isActive = true
        imageView.clipsToBounds = true
        imageView.setCornerRadius(20)
        return imageView
    }()
    
    private lazy var titleLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentStrong900.color,
                            numberOfLines: 0,
                            font: .bold04Compact)
        return label
    }()
    
    private lazy var descriptionLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentSub800.color,
                            numberOfLines: 0,
                            font: .body04Compact)
        return label
    }()
    
    private lazy var labelStackView: CKStackView = {
        let stackView = CKStackView(spacing: 4)
        stackView.addArrangedSubviews([titleLabel, descriptionLabel])
        return stackView
    }()
    
    private lazy var stackView: CKStackView = {
        let stackView = CKStackView(axis: .horizontal, alignment: .center, spacing: 8)
        stackView.addArrangedSubviews([groupImageView, labelStackView])
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.isUserInteractionEnabled = true
        stackView.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(didTappedImage)))
        return stackView
    }()
    
    private lazy var separatorView: CKSeparatorView = {
        let view = CKSeparatorView()
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    // MARK: - Members
    private weak var delegate: NotificationTrainingGroupRequestInfoTableViewCellDelegate?
    private var model: NotificationModel?
    
    // MARK: - Initializers
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        prepareUI()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        prepareUI()
    }
    
    private func prepareUI() {
        selectionStyle = .none
        backgroundColor = .clear
        contentView.backgroundColor = .clear
        
        contentView.addSubview(stackView)
        contentView.addSubview(separatorView)
        
        NSLayoutConstraint.activate([
            stackView.topAnchor.constraint(equalTo: contentView.topAnchor, constant: 16),
            stackView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
            stackView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16),
            stackView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor, constant: -16),
            
            separatorView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor),
            separatorView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            separatorView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor)
        ])
    }
    
    // MARK: - Custom Methods
    func configure(delegate: NotificationTrainingGroupRequestInfoTableViewCellDelegate? = nil,
                   model: NotificationModel) {
        self.delegate = delegate
        self.model = model
        groupImageView.setImage(with: model.image,
                                  placeholder: Asset.errorUserImage.image)
        titleLabel.text = model.title
        titleLabel.isHidden = model.title.isEmpty
        descriptionLabel.text = model.message
    }
    
    @objc private func didTappedImage() {
        guard let model else { return }
        delegate?.didTappedImageTrainingGroupRequestInfoInfo(model: model)
    }
}
