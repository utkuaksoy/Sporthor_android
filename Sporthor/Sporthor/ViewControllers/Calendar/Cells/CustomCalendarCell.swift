//
//  CustomCalendarCell.swift
//  Sporthor
//
//  Created by derTurke on 21.05.2025.
//
import UIKit
import ComponentKit
import CommonKit
import FSCalendar

final class CustomCalendarCell: FSCalendarCell {
    
    private lazy var dayLabel: CKLabel = {
        let label = CKLabel()
        return label
    }()
    
    private lazy var weekdayLabel: CKLabel = {
        let label = CKLabel()
        return label
    }()
    
    private lazy var stackView: CKStackView = {
        let stackView = CKStackView(alignment: .center)
        stackView.addArrangedSubviews([weekdayLabel, dayLabel])
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var backgroundCircle: UIView = {
        let view = UIView()
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupView()
    }

    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        setupView()
    }
    
    private func setupView() {
        backgroundCircle.addSubview(stackView)
        contentView.addSubview(backgroundCircle)
        
        NSLayoutConstraint.activate([
            backgroundCircle.topAnchor.constraint(equalTo: contentView.topAnchor),
            backgroundCircle.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            backgroundCircle.trailingAnchor.constraint(equalTo: contentView.trailingAnchor),
            backgroundCircle.bottomAnchor.constraint(equalTo: contentView.bottomAnchor),
            
            stackView.topAnchor.constraint(equalTo: backgroundCircle.topAnchor, constant: 8),
            stackView.leadingAnchor.constraint(equalTo: backgroundCircle.leadingAnchor, constant: 10),
            stackView.trailingAnchor.constraint(equalTo: backgroundCircle.trailingAnchor, constant: -10),
            stackView.bottomAnchor.constraint(equalTo: backgroundCircle.bottomAnchor, constant: -8)
        ])
    }
    
    func configure(with date: Date,
                   weekdayColor: UIColor = .white,
                   weekDayFont: UIFont = .bold04Compact,
                   dayColor: UIColor = .white,
                   dayFont: UIFont = .heading06,
                   spacing: CGFloat = 10,
                   isSelected: Bool,
                   selectedBackgroundColor: UIColor = UIColor(red: 1, green: 1, blue: 1, alpha: 0.1),
                   selectedBackgroundRadius: CGFloat = 8) {
        weekdayLabel.text = date.toString("EEEEE")
        weekdayLabel.textColor = weekdayColor
        weekdayLabel.font = weekDayFont
        
        let calendar = Calendar.current
        let day = calendar.component(.day, from: date)
        dayLabel.text = "\(day)"
        dayLabel.textColor = dayColor
        dayLabel.font = dayFont
        
        stackView.spacing = spacing
        backgroundCircle.backgroundColor = isSelected ? selectedBackgroundColor : .clear
        backgroundCircle.setCornerRadius(selectedBackgroundRadius)
    }
}
