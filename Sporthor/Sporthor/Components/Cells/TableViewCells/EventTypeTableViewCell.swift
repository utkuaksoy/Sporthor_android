//
//  EventTypeTableViewCell.swift
//  Sporthor
//
//  Created by derTurke on 26.05.2025.
//

import UIKit
import ComponentKit

import UIKit
import ComponentKit

protocol EventTypeTableViewCellDelegate: AnyObject {
    func didSelectEventType(with model: EventTypeModel)
    func addEventType()
}

extension EventTypeTableViewCellDelegate {
    func didSelectEventType(with model: EventTypeModel) {}
    func addEventType() {}
}

final class EventTypeTableViewCell: UITableViewCell {
    
    // MARK: - UI Elements
    
    private lazy var tagContainerStackView: UIStackView = {
        let stackView = UIStackView()
        stackView.axis = .vertical
        stackView.spacing = 8
        stackView.alignment = .leading
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var infoLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentStrong900.color,
                            numberOfLines: 0,
                            font: .body04Compact)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var addEventTypeButton: CKButton = {
        let button = CKButton(delegate: self,
                              title: "Etkinlik Tipi Ekle",
                              titleColor: DesignKitColorName.contentStrong900.color,
                              buttonBackgroundColor: .clear,
                              cornerRadius: 23,
                              borderWidth: 1,
                              borderColor: DesignKitColorName.borderStrong900.color,
                              font: .bold04Compact,
                              image: Asset.blackPlus.image,
                              imageTitleSpacing: 4,
                              tag: 1)
        button.translatesAutoresizingMaskIntoConstraints = false
        button.heightAnchor.constraint(equalToConstant: 46).isActive = true
        button.widthAnchor.constraint(equalToConstant: 161).isActive = true
        return button
    }()
    
    // MARK: - Members
    private weak var delegate: EventTypeTableViewCellDelegate?
    private var model: [EventTypeModel] = []
    private var type: EventTypeTagViewStyle = .calendar
    
    // MARK: - Init
    
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        prepareUI()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        prepareUI()
    }
    
    // MARK: - UI Setup
    
    private func prepareUI() {
        backgroundColor = .clear
        contentView.backgroundColor = .clear
        
        contentView.addSubview(tagContainerStackView)
        contentView.addSubview(infoLabel)
        contentView.addSubview(addEventTypeButton)
        
        NSLayoutConstraint.activate([
            tagContainerStackView.topAnchor.constraint(equalTo: contentView.topAnchor, constant: 16),
            tagContainerStackView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
            tagContainerStackView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16),
            
            infoLabel.topAnchor.constraint(equalTo: tagContainerStackView.bottomAnchor, constant: 16),
            infoLabel.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
            infoLabel.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16),
            
            addEventTypeButton.topAnchor.constraint(equalTo: infoLabel.bottomAnchor, constant: 16),
            addEventTypeButton.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
            addEventTypeButton.bottomAnchor.constraint(equalTo: contentView.bottomAnchor, constant: -16),
        ])
    }
    
    // MARK: - Configure
    
    func configure(delegate: EventTypeTableViewCellDelegate? = nil,
                   model: [EventTypeModel] = [],
                   type: EventTypeTagViewStyle = .calendar,
                   info: String = "",
                   isHiddenAddButtonType: Bool = false) {
        self.delegate = delegate
        self.model = model
        self.type = type
        infoLabel.text = info
        infoLabel.isHidden = info.isEmpty
        addEventTypeButton.isHidden = isHiddenAddButtonType
        layoutTags(model)
    }
    
    // MARK: - Layout Tags
    
    private func layoutTags(_ tags: [EventTypeModel]) {
        // Temizle
        tagContainerStackView.arrangedSubviews.forEach { $0.removeFromSuperview() }

        var currentRow = UIStackView()
        currentRow.axis = .horizontal
        currentRow.spacing = 8
        currentRow.alignment = .leading
        
        var currentRowWidth: CGFloat = 0
        let maxRowWidth = UIScreen.main.bounds.width - 32 // 16 leading + 16 trailing
        
        for (index, tag) in tags.enumerated() {
            let tagView = EventTypeTagView()
            tagView.configure(type: type, with: tag)
            tagView.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(handleTagTap(_:))))
            tagView.tag = index // Eğer EventTypeModel'de id varsa

            let fittingSize = tagView.systemLayoutSizeFitting(UIView.layoutFittingCompressedSize)
            let tagWidth = fittingSize.width
            
            if currentRowWidth + tagWidth > maxRowWidth {
                tagContainerStackView.addArrangedSubview(currentRow)
                currentRow = UIStackView()
                currentRow.axis = .horizontal
                currentRow.spacing = 8
                currentRow.alignment = .leading
                currentRowWidth = 0
            }
            
            currentRow.addArrangedSubview(tagView)
            currentRowWidth += tagWidth + 8
        }
        
        if currentRow.arrangedSubviews.count > 0 {
            tagContainerStackView.addArrangedSubview(currentRow)
        }
    }
    
    // MARK: - Actions
    
    @objc private func handleTagTap(_ gesture: UITapGestureRecognizer) {
        guard let view = gesture.view as? EventTypeTagView else { return }
        guard let tappedIndex = tagContainerStackView.arrangedSubviews
                .flatMap({ ($0 as? UIStackView)?.arrangedSubviews ?? [] })
                .firstIndex(of: view) else { return }
        let tappedModel = model[tappedIndex]
        delegate?.didSelectEventType(with: tappedModel)
    }
}

extension EventTypeTableViewCell: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        delegate?.addEventType()
    }
}
