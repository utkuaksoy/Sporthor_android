//
//  TitleDateAndHourTableViewCell.swift
//  Sporthor
//
//  Created by derTurke on 27.05.2025.
//

import UIKit
import ComponentKit

enum TitleDateAndHourTableViewCellType {
    case start
    case end
}

protocol TitleDateAndHourTableViewCellDelegate: AnyObject {
    func didTappedDate(_ type: TitleDateAndHourTableViewCellType)
    func didTappedHour(_ type: TitleDateAndHourTableViewCellType)
}

extension TitleDateAndHourTableViewCellDelegate {
    func didTappedDate(_ type: TitleDateAndHourTableViewCellType) {}
    func didTappedHour(_ type: TitleDateAndHourTableViewCellType) {}
}

final class TitleDateAndHourTableViewCell: UITableViewCell {
    // MARK: - UI Elements
    private lazy var titleLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentStrong900.color,
                            numberOfLines: 0,
                            font: .body03Compact)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var dateView: UIView = {
        let view = UIView()
        view.translatesAutoresizingMaskIntoConstraints = false
        view.backgroundColor = DesignKitColorName.backgroundWeak100.color
        view.setCornerRadius(8)
        view.isUserInteractionEnabled = true
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(didTappedDate))
        view.addGestureRecognizer(tapGesture)
        return view
    }()
    
    private lazy var dateLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentStrong900.color,
                            textAlignment: .center,
                            font: .body04Compact)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var hourView: UIView = {
        let view = UIView()
        view.translatesAutoresizingMaskIntoConstraints = false
        view.backgroundColor = DesignKitColorName.backgroundWeak100.color
        view.setCornerRadius(8)
        view.isUserInteractionEnabled = true
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(didTappedHour))
        view.addGestureRecognizer(tapGesture)
        return view
    }()
    
    private lazy var hourLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentStrong900.color,
                            textAlignment: .center,
                            font: .body04Compact)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var separatorView: UIView = {
        let view = UIView()
        view.backgroundColor = DesignKitColorName.borderSoft200.color
        view.translatesAutoresizingMaskIntoConstraints = false
        view.heightAnchor.constraint(equalToConstant: 1).isActive = true
        return view
    }()
    
    // MARK: - Members
    private var dateViewTrailingCons: NSLayoutConstraint!
    private weak var delegate: TitleDateAndHourTableViewCellDelegate?
    private var type: TitleDateAndHourTableViewCellType = .start
    
    // MARK: - Initialize
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        prepareUI()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        prepareUI()
    }
    
    private func prepareUI() {
        backgroundColor = .clear
        contentView.backgroundColor = .clear
        
        [titleLabel,
         dateView,
         hourView,
         dateLabel,
         hourLabel,
         separatorView].forEach {
            $0.translatesAutoresizingMaskIntoConstraints = false
            contentView.addSubview($0)
        }
        
        dateViewTrailingCons = dateView.trailingAnchor.constraint(equalTo: hourView.leadingAnchor, constant: -8)
        
        NSLayoutConstraint.activate([
            titleLabel.topAnchor.constraint(equalTo: contentView.topAnchor, constant: 16),
            titleLabel.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
            titleLabel.bottomAnchor.constraint(equalTo: contentView.bottomAnchor, constant: -16),
            
            hourView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16),
            hourView.centerYAnchor.constraint(equalTo: contentView.centerYAnchor),
            hourView.widthAnchor.constraint(equalToConstant: 72),
            hourView.heightAnchor.constraint(equalToConstant: 38),
            
            hourLabel.topAnchor.constraint(equalTo: hourView.topAnchor, constant: 8),
            hourLabel.leadingAnchor.constraint(equalTo: hourView.leadingAnchor, constant: 16),
            hourLabel.trailingAnchor.constraint(equalTo: hourView.trailingAnchor, constant: -16),
            hourLabel.bottomAnchor.constraint(equalTo: hourView.bottomAnchor, constant: -8),
            
            
            dateView.leadingAnchor.constraint(equalTo: titleLabel.trailingAnchor, constant: 8),
            dateView.centerYAnchor.constraint(equalTo: contentView.centerYAnchor),
            dateViewTrailingCons,
            dateView.heightAnchor.constraint(equalToConstant: 38),
            dateView.widthAnchor.constraint(equalToConstant: 140),
            
            dateLabel.topAnchor.constraint(equalTo: dateView.topAnchor, constant: 8),
            dateLabel.leadingAnchor.constraint(equalTo: dateView.leadingAnchor, constant: 16),
            dateLabel.trailingAnchor.constraint(equalTo: dateView.trailingAnchor, constant: -16),
            dateLabel.bottomAnchor.constraint(equalTo: dateView.bottomAnchor, constant: -8),
            
            separatorView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
            separatorView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16),
            separatorView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor)
        ])
    }
    
    // MARK: - Custom Methods
    func configure(
        delegate: TitleDateAndHourTableViewCellDelegate? = nil,
        type: TitleDateAndHourTableViewCellType,
        title: String,
        date: String,
        hour: String,
        isHiddenHourView: Bool = false
    ) {
        self.delegate = delegate
        self.type = type
        titleLabel.text = title
        dateLabel.text = date
        hourLabel.text = hour
        
        if isHiddenHourView {
            hourView.isHidden = true
            hourLabel.isHidden = true
            dateViewTrailingCons.isActive = false
            dateViewTrailingCons = dateView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16)
            dateViewTrailingCons.isActive = true
        } else {
            hourView.isHidden = false
            hourLabel.isHidden = false
            dateViewTrailingCons.isActive = false
            dateViewTrailingCons = dateView.trailingAnchor.constraint(equalTo: hourView.leadingAnchor, constant: -8)
            dateViewTrailingCons.isActive = true
        }
    }
    
    @objc private func didTappedDate() {
        delegate?.didTappedDate(type)
    }
    
    @objc private func didTappedHour() {
        delegate?.didTappedHour(type)
    }
}
    

