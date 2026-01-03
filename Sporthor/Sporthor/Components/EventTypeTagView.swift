//
//  EventTypeTagView.swift
//  Sporthor
//
//  Created by derTurke on 26.05.2025.
//

import UIKit
import ComponentKit
import DesignKit

enum EventTypeTagViewStyle {
    case calendar
    case role
}

final class EventTypeTagView: UIView {
    // Aynı UI elemanları
    private lazy var colorView: UIView = {
        let view = UIView()
        view.setCornerRadius(4)
        view.translatesAutoresizingMaskIntoConstraints = false
        view.widthAnchor.constraint(equalToConstant: 8).isActive = true
        view.heightAnchor.constraint(equalToConstant: 8).isActive = true
        return view
    }()
    
    private lazy var titleLabel: CKLabel = {
        let label = CKLabel(numberOfLines: 0, font: .body04Compact)
        return label
    }()
    
    private lazy var rightImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .center
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.widthAnchor.constraint(equalToConstant: 20).isActive = true
        imageView.heightAnchor.constraint(equalToConstant: 20).isActive = true
        return imageView
    }()
    
    private lazy var stackView: CKStackView = {
        let stackView = CKStackView(axis: .horizontal, alignment: .center, spacing: 8)
        stackView.addArrangedSubviews([colorView, titleLabel, rightImageView])
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    init() {
        super.init(frame: .zero)
        prepareUI()
    }

    required init?(coder: NSCoder) {
        super.init(coder: coder)
        prepareUI()
    }

    private func prepareUI() {
        addSubview(stackView)
        translatesAutoresizingMaskIntoConstraints = false

        NSLayoutConstraint.activate([
            stackView.topAnchor.constraint(equalTo: topAnchor, constant: 8),
            stackView.leadingAnchor.constraint(equalTo: leadingAnchor, constant: 8),
            stackView.trailingAnchor.constraint(equalTo: trailingAnchor, constant: -8),
            stackView.bottomAnchor.constraint(equalTo: bottomAnchor, constant: -8),
            heightAnchor.constraint(equalToConstant: 36)
        ])
    }

    func configure(type: EventTypeTagViewStyle = .calendar,
                   with model: EventTypeModel) {
        switch type {
        case .calendar:
            backgroundColor = model.isSelected ? DesignKitColorName.contentStrong900.color : .clear
            setBorderColor(model.isSelected ? .clear : DesignKitColorName.borderSoft200.color)
            setBorderWidth(model.isSelected ? 0 : 1)
            titleLabel.textColor = model.isSelected ? .white : DesignKitColorName.contentStrong900.color
            colorView.backgroundColor = UIColor(hex: model.detail)
            rightImageView.image = model.isSelected ? Asset.successWhite.image : Asset.blackPlus.image
        case .role:
            backgroundColor = model.isSelected ? DesignKitColorName.successLighter100.color : .clear
            setBorderColor(model.isSelected ? DesignKitColorName.successBase500.color : DesignKitColorName.borderSoft200.color)
            setBorderWidth(1)
            titleLabel.textColor = DesignKitColorName.contentStrong900.color
            colorView.isHidden = true
            rightImageView.isHidden = !model.isSelected
            rightImageView.image = Asset.successBlack.image
        }
        setCornerRadius(8)
        titleLabel.text = model.name
    }
}
